package com.sportsmatching.view;

import com.sportsmatching.dominio.Location;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.service.DistanciaService;
import com.sportsmatching.mvc.autenticacion.AuthController;
import com.sportsmatching.mvc.autenticacion.AuthView;
import com.sportsmatching.mvc.busqueda.BusquedaController;
import com.sportsmatching.mvc.busqueda.BusquedaView;
import com.sportsmatching.mvc.catalogos.CatalogoController;
import com.sportsmatching.mvc.catalogos.CatalogoView;
import com.sportsmatching.mvc.partido.controladores.PartidoCreacionController;
import com.sportsmatching.mvc.partido.controladores.PartidoEstadisticasController;
import com.sportsmatching.mvc.partido.controladores.PartidoGestionController;
import com.sportsmatching.mvc.partido.vistas.PartidoDetailView;
import com.sportsmatching.mvc.partido.vistas.PartidoFormView;
import com.sportsmatching.mvc.partido.vistas.PartidoListView;
import com.sportsmatching.mvc.registro.RegistroController;
import com.sportsmatching.mvc.registro.RegistroView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InteractiveMenu {
    private final Scanner scanner = new Scanner(System.in);
    private Usuario usuarioActual;
    
    // Controllers y Views
    private final RegistroController registroController;
    private final RegistroView registroView;
    private final AuthController authController;
    private final AuthView authView;
    private final BusquedaController busquedaController;
    private final BusquedaView busquedaView;
    private final CatalogoController catalogoController;
    private final CatalogoView catalogoView;
    private final PartidoCreacionController partidoCreacionController;
    private final PartidoGestionController partidoGestionController;
    private final PartidoEstadisticasController partidoEstadisticasController;
    private final PartidoListView partidoListView;
    private final PartidoDetailView partidoDetailView;
    private final PartidoFormView partidoFormView;
    private final DistanciaService distanciaService;

    public InteractiveMenu(RegistroController registroController, RegistroView registroView,
                          AuthController authController, AuthView authView,
                          BusquedaController busquedaController, BusquedaView busquedaView,
                          CatalogoController catalogoController, CatalogoView catalogoView,
                          PartidoCreacionController partidoCreacionController,
                          PartidoGestionController partidoGestionController,
                          PartidoEstadisticasController partidoEstadisticasController,
                          PartidoListView partidoListView,
                          PartidoDetailView partidoDetailView,
                          PartidoFormView partidoFormView) {
        this.registroController = registroController;
        this.registroView = registroView;
        this.authController = authController;
        this.authView = authView;
        this.busquedaController = busquedaController;
        this.busquedaView = busquedaView;
        this.catalogoController = catalogoController;
        this.catalogoView = catalogoView;
        this.partidoCreacionController = partidoCreacionController;
        this.partidoGestionController = partidoGestionController;
        this.partidoEstadisticasController = partidoEstadisticasController;
        this.partidoListView = partidoListView;
        this.partidoDetailView = partidoDetailView;
        this.partidoFormView = partidoFormView;
        this.distanciaService = new DistanciaService();
    }

    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Sistema de Gestión de Partidos       ║");
        System.out.println("║  Deportivos                           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        while (true) {
            if (usuarioActual == null) {
                mostrarMenuNoAutenticado();
            } else {
                mostrarMenuAutenticado();
            }
        }
    }

    private void mostrarMenuNoAutenticado() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar Sesión");
        System.out.println("3. Ver Catálogos");
        System.out.println("4. Buscar Partidos");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1 -> registrarUsuario();
            case 2 -> iniciarSesion();
            case 3 -> verCatalogos();
            case 4 -> buscarPartidos();
            case 0 -> {
                System.out.println("¡Hasta luego!");
                System.exit(0);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private void mostrarMenuAutenticado() {
        System.out.println("\n=== MENÚ PRINCIPAL (Usuario: " + usuarioActual.getUsername() + ") ===");
        System.out.println("1. Crear Partido");
        System.out.println("2. Buscar Partidos");
        System.out.println("3. Ver Catálogos");
        System.out.println("4. Gestionar Partido");
        System.out.println("5. Agregar Estadísticas/Comentarios");
        System.out.println("6. Cerrar Sesión");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1 -> crearPartido();
            case 2 -> buscarPartidos();
            case 3 -> verCatalogos();
            case 4 -> gestionarPartido();
            case 5 -> agregarEstadisticasComentarios();
            case 6 -> {
                authController.logout();
                usuarioActual = null;
                System.out.println("Sesión cerrada");
            }
            case 0 -> {
                System.out.println("¡Hasta luego!");
                System.exit(0);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private void registrarUsuario() {
        registroView.mostrarFormulario();
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        List<Nivel> niveles = catalogoController.obtenerNiveles();
        System.out.println("Niveles disponibles:");
        for (int i = 0; i < niveles.size(); i++) {
            System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
        }
        System.out.print("Seleccione nivel: ");
        int nivelIdx = scanner.nextInt() - 1;
        scanner.nextLine();
        Nivel nivel = niveles.get(nivelIdx);
        
        List<Deporte> deportes = catalogoController.obtenerDeportes();
        System.out.println("Deportes disponibles:");
        for (int i = 0; i < deportes.size(); i++) {
            System.out.println((i + 1) + ". " + deportes.get(i).getNombre());
        }
        System.out.print("Seleccione deporte favorito: ");
        int deporteIdx = scanner.nextInt() - 1;
        scanner.nextLine();
        Deporte deporteFavorito = deportes.get(deporteIdx);
        
        System.out.print("Ubicación - Latitud: ");
        double latitud = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Ubicación - Longitud: ");
        double longitud = scanner.nextDouble();
        scanner.nextLine();
        
        Location ubicacion = new Location(latitud, longitud);
        
        registroController.registrarUsuario(username, email, password, nivel, deporteFavorito, ubicacion);
    }

    private void iniciarSesion() {
        authView.mostrarLogin();
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        usuarioActual = authController.login(username, password);
        if (usuarioActual != null) {
            System.out.println("✓ Sesión iniciada correctamente");
        }
    }

    private void verCatalogos() {
        System.out.println("\n--- CATÁLOGOS ---");
        catalogoController.obtenerDeportes();
        catalogoController.obtenerNiveles();
    }

    private void buscarPartidos() {
        busquedaView.mostrarFormularioBusqueda();
        
        // Pedir ubicación del usuario
        System.out.print("Ingrese su ubicación - Latitud: ");
        double latitud = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Ingrese su ubicación - Longitud: ");
        double longitud = scanner.nextDouble();
        scanner.nextLine();
        
        Location ubicacionUsuario = new Location(latitud, longitud);
        
        Map<String, Object> criterios = new HashMap<>();
        criterios.put("ubicacionUsuario", ubicacionUsuario);
        
        // Si hay usuario autenticado, usar sus preferencias
        if (usuarioActual != null) {
            criterios.put("nivelUsuario", usuarioActual);
            criterios.put("deporteFavorito", usuarioActual);
            System.out.print("¿Filtrar por disponibilidad? (s/n): ");
            String filtro = scanner.nextLine();
            if (filtro.equalsIgnoreCase("s")) {
                criterios.put("disponibilidad", true);
            }
        } else {
            System.out.print("¿Filtrar por disponibilidad? (s/n): ");
            String filtro = scanner.nextLine();
            if (filtro.equalsIgnoreCase("s")) {
                criterios.put("disponibilidad", true);
            }
        }
        
        List<Partido> resultados = busquedaController.buscarPartidos(criterios);
        
        if (!resultados.isEmpty()) {
            System.out.println("\n=== Partidos encontrados ===");
            for (Partido p : resultados) {
                double distancia = distanciaService.calcularDistancia(ubicacionUsuario, p.getUbicacion());
                System.out.println("ID: " + p.getId() + 
                    " | Deporte: " + p.getDeporte().getNombre() + 
                    " | Ubicación: " + p.getUbicacion().getDescripcion() +
                    " | Distancia: " + String.format("%.2f", distancia) + " km" +
                    " | Estado: " + p.getEstado().getNombreEstado() +
                    " | Cupos: " + p.getPartidoJugadores().obtenerCantidadDisponible() + "/" + p.getJugadoresRequeridos());
            }
            
            System.out.println("\n¿Ver detalle de algún partido? (s/n): ");
            String verDetalle = scanner.nextLine();
            if (verDetalle.equalsIgnoreCase("s")) {
                System.out.print("Ingrese ID del partido: ");
                Long id = scanner.nextLong();
                scanner.nextLine();
                resultados.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresentOrElse(
                        busquedaView::mostrarDetallePartido,
                        () -> System.out.println("Partido no encontrado")
                    );
            }
        } else {
            System.out.println("No se encontraron partidos que coincidan con los criterios de búsqueda.");
        }
    }

    private void crearPartido() {
        partidoFormView.mostrarFormularioCreacion();
        
        List<Deporte> deportes = catalogoController.obtenerDeportes();
        System.out.println("Deportes disponibles:");
        for (int i = 0; i < deportes.size(); i++) {
            System.out.println((i + 1) + ". " + deportes.get(i).getNombre());
        }
        System.out.print("Seleccione deporte: ");
        int deporteIdx = scanner.nextInt() - 1;
        scanner.nextLine();
        Deporte deporte = deportes.get(deporteIdx);
        
        System.out.print("Jugadores requeridos: ");
        int jugadoresRequeridos = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Ubicación - Descripción: ");
        String descripcionUbicacion = scanner.nextLine();
        
        System.out.print("Ubicación - Latitud: ");
        double latitud = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Ubicación - Longitud: ");
        double longitud = scanner.nextDouble();
        scanner.nextLine();
        
        Location ubicacion = new Location(latitud, longitud, descripcionUbicacion);
        
        System.out.print("Duración (minutos): ");
        int duracion = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Fecha y hora (YYYY-MM-DD HH:MM): ");
        String fechaStr = scanner.nextLine();
        // Convertir formato de fecha a ISO
        String fechaISO = fechaStr.replace(" ", "T");
        if (!fechaISO.contains("T")) {
            fechaISO = fechaStr + "T00:00";
        }
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaISO);
        } catch (Exception e) {
            System.out.println("Error: Formato de fecha inválido. Use YYYY-MM-DD HH:MM");
            return;
        }
        
        List<Nivel> niveles = catalogoController.obtenerNiveles();
        System.out.println("Nivel mínimo:");
        for (int i = 0; i < niveles.size(); i++) {
            System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
        }
        System.out.print("Seleccione: ");
        int nivelMinIdx = scanner.nextInt() - 1;
        scanner.nextLine();
        Nivel nivelMin = niveles.get(nivelMinIdx);
        
        System.out.println("Nivel máximo:");
        for (int i = 0; i < niveles.size(); i++) {
            System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
        }
        System.out.print("Seleccione: ");
        int nivelMaxIdx = scanner.nextInt() - 1;
        scanner.nextLine();
        Nivel nivelMax = niveles.get(nivelMaxIdx);
        
        Partido partido = partidoCreacionController.crearPartido(
            deporte, usuarioActual, jugadoresRequeridos, ubicacion, fechaHora, duracion, nivelMin, nivelMax
        );
        
        if (partido != null) {
            System.out.println("✓ Partido creado exitosamente (ID: " + partido.getId() + ")");
        }
    }

    private void gestionarPartido() {
        System.out.print("Ingrese ID del partido: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        
        // Aquí necesitaríamos obtener el partido del repositorio
        // Por ahora mostramos un mensaje
        System.out.println("Funcionalidad de gestión de partido");
        System.out.println("1. Inscribir jugador");
        System.out.println("2. Confirmar partido");
        System.out.println("3. Cancelar partido");
        System.out.print("Seleccione: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        // La implementación completa requeriría obtener el partido del repositorio
        System.out.println("(Funcionalidad pendiente de implementación completa)");
    }

    private void agregarEstadisticasComentarios() {
        System.out.print("Ingrese ID del partido: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        
        System.out.println("1. Agregar estadísticas");
        System.out.println("2. Agregar comentario");
        System.out.print("Seleccione: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        // La implementación completa requeriría obtener el partido del repositorio
        System.out.println("(Funcionalidad pendiente de implementación completa)");
    }
}

