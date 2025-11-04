package com.sportsmatching.view;

import com.sportsmatching.controller.MatchController;
import com.sportsmatching.controller.UserController;
import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Location;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;
import com.sportsmatching.service.LocationService;
import com.sportsmatching.service.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

public class InteractiveMenu {
    private final Scanner scanner;
    private final UserController userController;
    private final MatchController matchController;
    private final SessionService sessionService;
    private final ReferenceData ref;
    private final LocationService locationService;

    public InteractiveMenu(UserController userController, MatchController matchController, SessionService sessionService) {
        this.scanner = new Scanner(System.in);
        this.userController = userController;
        this.matchController = matchController;
        this.sessionService = sessionService;
        this.ref = ReferenceData.get();
        this.locationService = new LocationService();
    }

    public void run() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  SISTEMA DE GESTIÓN DE PARTIDOS DEPORTIVOS");
        System.out.println("=".repeat(50) + "\n");

        while (true) {
            if (!sessionService.isLoggedIn()) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Salir");
        System.out.print("\nSeleccione una opción: ");

        int option = readInt();
        switch (option) {
            case 1 -> register();
            case 2 -> login();
            case 3 -> {
                System.out.println("\n¡Hasta luego!");
                System.exit(0);
            }
            default -> System.out.println("❌ Opción inválida");
        }
    }

    private void showUserMenu() {
        User user = sessionService.getCurrentUser();
        System.out.println("\n=== MENÚ DE USUARIO ===");
        System.out.println("Usuario: " + user.getUsername() + " (" + user.getEmail() + ")");
        System.out.println("\n1. Crear partido");
        System.out.println("2. Buscar partidos");
        System.out.println("3. Unirse a un partido");
        System.out.println("4. Ver mis partidos");
        System.out.println("5. Cerrar sesión");
        System.out.print("\nSeleccione una opción: ");

        int option = readInt();
        switch (option) {
            case 1 -> createMatch();
            case 2 -> searchMatches();
            case 3 -> joinMatch();
            case 4 -> showMyMatches();
            case 5 -> {
                sessionService.logout();
                System.out.println("✓ Sesión cerrada");
            }
            default -> System.out.println("❌ Opción inválida");
        }
    }

    private void register() {
        System.out.println("\n=== REGISTRO ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.println("\nDeportes disponibles:");
        int i = 1;
        for (SportType sport : ref.allSports()) {
            System.out.println(i + ". " + sport.getName());
            i++;
        }
        System.out.print("Seleccione deporte favorito (número): ");
        int sportChoice = readInt() - 1;
        SportType sport = ref.allSports().stream().skip(sportChoice).findFirst()
                .orElse(ref.sport("OTHER"));

        System.out.println("\nNiveles disponibles:");
        System.out.println("1. BEGINNER");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        System.out.print("Seleccione nivel (1-3): ");
        int levelChoice = readInt();
        SkillLevel level = switch (levelChoice) {
            case 1 -> ref.skill("BEGINNER");
            case 2 -> ref.skill("INTERMEDIATE");
            case 3 -> ref.skill("ADVANCED");
            default -> ref.skill("INTERMEDIATE");
        };

        System.out.println("\n=== UBICACIÓN ===");
        System.out.println("Ingrese sus coordenadas (puede usar Google Maps para obtenerlas):");
        System.out.print("Latitud (-90 a 90, ej. -34.6037 para Buenos Aires): ");
        double lat = readDouble();
        System.out.print("Longitud (-180 a 180, ej. -58.3816 para Buenos Aires): ");
        double lon = readDouble();
        Location userLocation = new Location(lat, lon);

        try {
            userController.registerUser(username, email, password, sport, level, userLocation);
            System.out.println("✓ Usuario registrado exitosamente");
        } catch (Exception e) {
            System.out.println("❌ Error al registrar: " + e.getMessage());
        }
    }

    private void login() {
        System.out.println("\n=== INICIAR SESIÓN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (sessionService.login(username, password)) {
            System.out.println("✓ Sesión iniciada correctamente");
        } else {
            System.out.println("❌ Usuario o contraseña incorrectos");
        }
    }

    private void createMatch() {
        System.out.println("\n=== CREAR PARTIDO ===");
        
        System.out.println("Deportes disponibles:");
        int i = 1;
        for (SportType sport : ref.allSports()) {
            System.out.println(i + ". " + sport.getName());
            i++;
        }
        System.out.print("Seleccione deporte (número): ");
        int sportChoice = readInt() - 1;
        SportType sport = ref.allSports().stream().skip(sportChoice).findFirst()
                .orElse(ref.sport("FOOTBALL"));

        System.out.print("Jugadores requeridos: ");
        int requiredPlayers = readInt();

        System.out.print("Duración (minutos): ");
        int duration = readInt();

        System.out.println("\n=== UBICACIÓN DEL PARTIDO ===");
        System.out.print("Descripción de la ubicación (ej. 'Cancha de fútbol en Parque Centenario'): ");
        String locationDescription = scanner.nextLine().trim();
        
        System.out.println("Coordenadas del partido:");
        System.out.print("Latitud (-90 a 90): ");
        double lat = readDouble();
        System.out.print("Longitud (-180 a 180): ");
        double lon = readDouble();
        Location matchLocation = new Location(lat, lon);

        System.out.print("Fecha y hora (dd/MM/yyyy HH:mm): ");
        String dateTimeStr = scanner.nextLine().trim();
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("❌ Formato de fecha inválido. Usando fecha actual.");
            dateTime = LocalDateTime.now();
        }

        try {
            Match match = matchController.create(sport, requiredPlayers, duration, matchLocation, locationDescription, dateTime);
            // Notificaciones se envían automáticamente por email desde MatchService
            
            System.out.println("✓ Partido creado exitosamente");
            System.out.println("  ID: " + match.getId());
            System.out.println("  Deporte: " + match.getSportType().getName());
            System.out.println("  Ubicación: " + match.getLocationDescription());
            System.out.println("  Coordenadas: " + match.getLocation());
            System.out.println("  Fecha: " + match.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        } catch (Exception e) {
            System.out.println("❌ Error al crear partido: " + e.getMessage());
        }
    }

    private void searchMatches() {
        System.out.println("\n=== BUSCAR PARTIDOS ===");
        
        if (!sessionService.isLoggedIn()) {
            System.out.println("❌ Debe iniciar sesión para buscar partidos");
            return;
        }
        
        User currentUser = sessionService.getCurrentUser();
        Location userLocation = currentUser.getLocation();
        
        System.out.println("Deportes disponibles:");
        int i = 1;
        for (SportType sport : ref.allSports()) {
            System.out.println(i + ". " + sport.getName());
            i++;
        }
        System.out.print("Seleccione deporte (número, 0 para todos): ");
        int sportChoice = readInt() - 1;
        SportType sport = null;
        if (sportChoice >= 0 && sportChoice < ref.allSports().size()) {
            sport = ref.allSports().stream().skip(sportChoice).findFirst().orElse(null);
        }

        Collection<Match> matches = matchController.searchNearby(sport, userLocation);
        
        if (matches.isEmpty()) {
            String sportName = sport != null ? sport.getName() : "ningún deporte";
            System.out.println("No hay partidos disponibles para " + sportName);
        } else {
            System.out.println("\n📍 Partidos encontrados (ordenados por cercanía):");
            System.out.println("Tu ubicación: " + userLocation);
            System.out.println("-".repeat(60));
            i = 1;
            for (Match match : matches) {
                double distance = locationService.calculateDistance(userLocation, match.getLocation());
                String distanceStr = locationService.formatDistance(distance);
                
                System.out.println("\n" + i + ". " + match.getSportType().getName());
                System.out.println("   ID: " + match.getShortId());
                System.out.println("   Ubicación: " + match.getLocationDescription());
                System.out.println("   Coordenadas: " + match.getLocation());
                System.out.println("   📏 Distancia: " + distanceStr);
                System.out.println("   📅 Fecha: " + match.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("   👥 Jugadores: " + match.getPlayers().size() + "/" + match.getRequiredPlayers());
                System.out.println("   ⚡ Estado: " + match.getState().name());
                i++;
            }
        }
    }

    private void joinMatch() {
        System.out.println("\n=== UNIRSE A PARTIDO ===");
        System.out.println("Ejemplo de ID: MAT-A3B9X2 (o solo A3B9X2)");
        System.out.print("Ingrese el ID del partido: ");
        String matchId = scanner.nextLine().trim().toUpperCase();

        User user = sessionService.getCurrentUser();
        try {
            matchController.join(matchId, user.getUsername());
            System.out.println("✓ Te has unido al partido exitosamente");
            System.out.println("  Se te enviará un email con los detalles");
        } catch (Exception e) {
            System.out.println("❌ Error al unirse al partido: " + e.getMessage());
        }
    }

    private void showMyMatches() {
        System.out.println("\n=== MIS PARTIDOS ===");
        // TODO: Implementar cuando tengamos método para buscar partidos por usuario
        System.out.println("Funcionalidad en desarrollo");
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private double readDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("❌ Entrada inválida. Ingrese un número: ");
            }
        }
    }
}

