package com.sportsmatching.view;

import com.sportsmatching.controller.MatchController;
import com.sportsmatching.controller.UserController;
import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.SportType;
import com.sportsmatching.model.User;
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

    public InteractiveMenu(UserController userController, MatchController matchController, SessionService sessionService) {
        this.scanner = new Scanner(System.in);
        this.userController = userController;
        this.matchController = matchController;
        this.sessionService = sessionService;
        this.ref = ReferenceData.get();
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

        try {
            userController.registerUser(username, email, password, sport, level);
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

        System.out.print("Ubicación: ");
        String location = scanner.nextLine().trim();

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
            Match match = matchController.create(sport, requiredPlayers, duration, location, dateTime);
            // Notificaciones se envían automáticamente por email desde MatchService
            
            System.out.println("✓ Partido creado exitosamente");
            System.out.println("  ID: " + match.getId());
            System.out.println("  Deporte: " + match.getSportType().getName());
            System.out.println("  Ubicación: " + match.getLocation());
            System.out.println("  Fecha: " + match.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        } catch (Exception e) {
            System.out.println("❌ Error al crear partido: " + e.getMessage());
        }
    }

    private void searchMatches() {
        System.out.println("\n=== BUSCAR PARTIDOS ===");
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

        Collection<Match> matches = matchController.search(sport);
        
        if (matches.isEmpty()) {
            System.out.println("No hay partidos disponibles para " + sport.getName());
        } else {
            System.out.println("\nPartidos encontrados:");
            i = 1;
            for (Match match : matches) {
                System.out.println("\n" + i + ". " + match.getSportType().getName());
                System.out.println("   ID: " + match.getId());
                System.out.println("   Ubicación: " + match.getLocation());
                System.out.println("   Fecha: " + match.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("   Jugadores: " + match.getPlayers().size() + "/" + match.getRequiredPlayers());
                System.out.println("   Estado: " + match.getState().name());
                i++;
            }
        }
    }

    private void joinMatch() {
        System.out.println("\n=== UNIRSE A PARTIDO ===");
        System.out.print("Ingrese el ID del partido: ");
        String matchId = scanner.nextLine().trim();

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
}

