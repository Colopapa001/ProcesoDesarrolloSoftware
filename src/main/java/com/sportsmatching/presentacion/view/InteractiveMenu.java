package com.sportsmatching.presentacion.view;

import com.sportsmatching.dominio.Location;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.aplicacion.servicios.DistanciaService;
import com.sportsmatching.presentacion.mvc.autenticacion.AuthController;
import com.sportsmatching.presentacion.mvc.autenticacion.AuthView;
import com.sportsmatching.presentacion.mvc.busqueda.BusquedaController;
import com.sportsmatching.presentacion.mvc.busqueda.BusquedaView;
import com.sportsmatching.presentacion.mvc.catalogos.CatalogoController;
import com.sportsmatching.presentacion.mvc.catalogos.CatalogoView;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoCreacionController;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoEstadisticasController;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoGestionController;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoDetailView;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoFormView;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoListView;
import com.sportsmatching.presentacion.mvc.registro.RegistroController;
import com.sportsmatching.presentacion.mvc.registro.RegistroView;
import com.sportsmatching.infra.MockDomainDataStore; 


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

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

        // Mostrar resumen de datos mock cargados (opcional)
        try {
            System.out.println("Datos cargados: usuarios=" + MockDomainDataStore.allUsuarios().size()
                + " partidos=" + MockDomainDataStore.allPartidos().size());
        } catch (NoClassDefFoundError | Exception ignored) {
            // Si no usás MockDomainDataStore (no lo agregaste), ignorar
        }

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
        
        try {
            System.out.print("Username [default: emma]: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) username = "emma";
            
            
            // Validación de email
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            String email;
            while (true) {
                System.out.print("Email [default: emma.maidana@gmail.com]: ");
                email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    email = "emma.maidana@gmail.com";
                    break;
                }
                if (emailPattern.matcher(email).matches()) {
                    break;
                } else {
                    System.out.println("Email inválido. Ingrese un email con formato válido (ej: usuario@dominio.com).");
                }
            }
            
            System.out.print("Password [default: emma]: ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) password = "emma";
            
            List<Nivel> niveles = catalogoController.obtenerNivelesSinMostrar();
            System.out.println("Niveles disponibles:");
            for (int i = 0; i < niveles.size(); i++) {
                System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
            }
            // Validar selección de nivel: obligatoria y debe ser 1..niveles.size()
            int nivelIdx = -1;
            while (nivelIdx < 0 || nivelIdx >= niveles.size()) {
                System.out.print("Seleccione nivel [default: 1]: ");
                String nivelInput = scanner.nextLine().trim();
                if (nivelInput.isEmpty()) {
                    nivelIdx = 0; // default 1
                    break;
                }
                try {
                    int sel = Integer.parseInt(nivelInput);
                    if (sel >= 1 && sel <= niveles.size()) {
                        nivelIdx = sel - 1;
                    } else {
                        System.out.println("Opción inválida. Ingrese un número entre 1 y " + niveles.size() + ".");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrada inválida. Ingrese el número correspondiente al nivel.");
                }
            }
            Nivel nivel = niveles.get(nivelIdx);
            
            List<Deporte> deportes = catalogoController.obtenerDeportesSinMostrar();
            System.out.println("Deportes disponibles:");
            for (int i = 0; i < deportes.size(); i++) {
                System.out.println((i + 1) + ". " + deportes.get(i).getNombre());
            }
            int deporteIdx = -1;
            while (deporteIdx < 0 || deporteIdx >= deportes.size()) {
                System.out.print("Seleccione deporte favorito [default: 1]: ");
                String deporteInput = scanner.nextLine().trim();
                if (deporteInput.isEmpty()) {
                    deporteIdx = 0;
                    break;
                }
                try {
                    int sel = Integer.parseInt(deporteInput);
                    if (sel >= 1 && sel <= deportes.size()) {
                        deporteIdx = sel - 1;
                    } else {
                        System.out.println("Opción inválida. Ingrese un número entre 1 y " + deportes.size() + ".");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Entrada inválida. Ingrese el número correspondiente al deporte.");
                }
            }
            Deporte deporteFavorito = deportes.get(deporteIdx);
            
            // Pedir descripción de ubicación y usar coordenadas por defecto de Buenos Aires
            System.out.print("Ubicación - Descripción [default: Buenos Aires]: ");
            String descripcionUbicacion = scanner.nextLine().trim();
            if (descripcionUbicacion.isEmpty()) descripcionUbicacion = "Buenos Aires";
            
            // Coordenadas por defecto de Buenos Aires
            double latitud = -34.6037;
            double longitud = -58.3816;
            
            Location ubicacion = new Location(latitud, longitud, descripcionUbicacion);
            
            boolean exito = registroController.registrarUsuario(username, email, password, nivel, deporteFavorito, ubicacion);
            if (exito) {
                System.out.println("✓ Usuario registrado exitosamente");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
        }
    }

    private void iniciarSesion() {
        authView.mostrarLogin();
        
        System.out.print("Username [default: emma]: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) username = "emma";
        
        System.out.print("Password [default: emma]: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) password = "emma";
        
        try {
            usuarioActual = authController.login(username, password);
            if (usuarioActual != null) {
                System.out.println("✓ Sesión iniciada correctamente");
                System.out.println("Bienvenido, " + usuarioActual.getUsername() + "!");
            }
        } catch (Exception e) {
            // El error ya se muestra en el AuthController
            System.out.println("\n💡 Sugerencia: Si no tienes una cuenta, regístrate primero (opción 1)");
        }
    }

    private void verCatalogos() {
        System.out.println("\n--- CATÁLOGOS ---");
        catalogoController.obtenerDeportes();
        catalogoController.obtenerNiveles();
    }

    // ...existing code...
    private void buscarPartidos() {
        busquedaView.mostrarFormularioBusqueda();

        // Usar ubicación del usuario si está autenticado, sino usar valores por defecto
        Location ubicacionUsuario;
        if (usuarioActual != null && usuarioActual.getUbicacion() != null) {
            ubicacionUsuario = usuarioActual.getUbicacion();
            System.out.println("✓ Usando tu ubicación registrada: " + ubicacionUsuario.getDescripcion());
        } else {
            System.out.print("Ubicación - Descripción [default: Buenos Aires]: ");
            String descripcionUbicacion = scanner.nextLine().trim();
            if (descripcionUbicacion.isEmpty()) descripcionUbicacion = "Buenos Aires";

            // Coordenadas por defecto de Buenos Aires
            double latitud = -34.6037;
            double longitud = -58.3816;

            ubicacionUsuario = new Location(latitud, longitud, descripcionUbicacion);
        }

        Map<String, Object> criterios = new HashMap<>();
        criterios.put("ubicacionUsuario", ubicacionUsuario);

        boolean filtrarDisponibilidad = false;
        // Si hay usuario autenticado, usar sus preferencias
        String nivelUsuarioStr = null;
        String deportePrefStr = null;
        if (usuarioActual != null) {
            criterios.put("nivelUsuario", usuarioActual);
            criterios.put("deporteFavorito", usuarioActual);
            if (usuarioActual.getNivel() != null) {
                nivelUsuarioStr = usuarioActual.getNivel().getNombre();
            }
            if (usuarioActual.getDeporteFavorito() != null) {
                deportePrefStr = usuarioActual.getDeporteFavorito().getNombre();
            }
            System.out.print("¿Filtrar por disponibilidad? (s/n): ");
            String filtro = scanner.nextLine();
            if (filtro.equalsIgnoreCase("s")) {
                criterios.put("disponibilidad", true);
                filtrarDisponibilidad = true;
            }
        } else {
            System.out.print("¿Filtrar por disponibilidad? (s/n): ");
            String filtro = scanner.nextLine();
            if (filtro.equalsIgnoreCase("s")) {
                criterios.put("disponibilidad", true);
                filtrarDisponibilidad = true;
            }
        }

        List<Partido> resultados = busquedaController.buscarPartidos(criterios);

        // Si controller no devuelve resultados, usar fallback al MockDomainDataStore (datos mock cargados)
        if (resultados == null || resultados.isEmpty()) {
            try {
                String ubicDesc = ubicacionUsuario != null ? ubicacionUsuario.getDescripcion() : "";
                var mockResultados = MockDomainDataStore.searchPartidos(ubicDesc, filtrarDisponibilidad, nivelUsuarioStr, deportePrefStr);

                if (mockResultados != null && !mockResultados.isEmpty()) {
                    System.out.println("\n=== Resultados (desde datos mock) ===");
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    for (var p : mockResultados) {
                        int inscritos = (p.partidoJugadores == null || p.partidoJugadores.jugadores == null)
                                ? 0 : p.partidoJugadores.jugadores.size();
                        System.out.println("ID: " + p.id +
                                " | Deporte: " + p.deporte +
                                " | Ubicación: " + (p.ubicacion != null ? p.ubicacion.descripcion : "n/d") +
                                " | Fecha: " + (p.fechaHora != null ? p.fechaHora.format(fmt) : "n/d") +
                                " | Cupos: " + inscritos + "/" + p.jugadoresRequeridos +
                                " | Nivel: " + p.nivelMin + "-" + p.nivelMax +
                                " | Organizador: " + p.organizadorUsername);
                    }
                    return;
                }
            } catch (NoClassDefFoundError | Exception ignored) {
                // Si no existe MockDomainDataStore o falla, continuar
            }

            System.out.println("No se encontraron partidos que coincidan con los criterios de búsqueda.");
            return;
        }

        // Si el controlador devolvió resultados, mostrarlos normalmente
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
    }


    private void crearPartido() {
        partidoFormView.mostrarFormularioCreacion();
        
        try {
            List<Deporte> deportes = catalogoController.obtenerDeportesSinMostrar();
            System.out.println("Deportes disponibles:");
            for (int i = 0; i < deportes.size(); i++) {
                System.out.println((i + 1) + ". " + deportes.get(i).getNombre());
            }
            System.out.print("Seleccione deporte [default: 1]: ");
            String deporteInput = scanner.nextLine().trim();
            int deporteIdx = deporteInput.isEmpty() ? 0 : Integer.parseInt(deporteInput) - 1;
            Deporte deporte = deportes.get(deporteIdx);
            
            System.out.print("Jugadores requeridos [default: 10]: ");
            String jugadoresInput = scanner.nextLine().trim();
            int jugadoresRequeridos = jugadoresInput.isEmpty() ? 10 : Integer.parseInt(jugadoresInput);
            
            // Usar ubicación del usuario si está autenticado, sino pedir descripción
            Location ubicacion;
            if (usuarioActual != null && usuarioActual.getUbicacion() != null) {
                System.out.print("Ubicación - Descripción [default: usar mi ubicación]: ");
                String descripcionUbicacion = scanner.nextLine().trim();
                if (descripcionUbicacion.isEmpty()) {
                    // Usar ubicación del usuario
                    ubicacion = usuarioActual.getUbicacion();
                    System.out.println("✓ Usando tu ubicación registrada: " + ubicacion.getDescripcion());
                } else {
                    // Usar coordenadas del usuario pero con nueva descripción
                    ubicacion = new Location(
                        usuarioActual.getUbicacion().getLatitud(),
                        usuarioActual.getUbicacion().getLongitud(),
                        descripcionUbicacion
                    );
                }
            } else {
                System.out.print("Ubicación - Descripción [default: Cancha Central]: ");
                String descripcionUbicacion = scanner.nextLine().trim();
                if (descripcionUbicacion.isEmpty()) descripcionUbicacion = "Cancha Central";
                
                // Coordenadas por defecto de Buenos Aires
                double latitud = -34.6037;
                double longitud = -58.3816;
                
                ubicacion = new Location(latitud, longitud, descripcionUbicacion);
            }
            
            System.out.print("Duración (minutos) [default: 90]: ");
            String duracionInput = scanner.nextLine().trim();
            int duracion = duracionInput.isEmpty() ? 90 : Integer.parseInt(duracionInput);
            
            System.out.print("Fecha y hora (YYYY-MM-DD HH:MM) [default: mañana 18:00]: ");
            String fechaStr = scanner.nextLine().trim();
            LocalDateTime fechaHora;
            if (fechaStr.isEmpty()) {
                fechaHora = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0);
            } else {
                // Convertir formato de fecha a ISO
                String fechaISO = fechaStr.replace(" ", "T");
                if (!fechaISO.contains("T")) {
                    fechaISO = fechaStr + "T00:00";
                }
                try {
                    fechaHora = LocalDateTime.parse(fechaISO);
                } catch (Exception e) {
                    System.out.println("Error: Formato de fecha inválido. Use YYYY-MM-DD HH:MM");
                    return;
                }
            }
            
            List<Nivel> niveles = catalogoController.obtenerNivelesSinMostrar();
            System.out.println("Nivel mínimo:");
            for (int i = 0; i < niveles.size(); i++) {
                System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
            }
            System.out.print("Seleccione [default: 1]: ");
            String nivelMinInput = scanner.nextLine().trim();
            int nivelMinIdx = nivelMinInput.isEmpty() ? 0 : Integer.parseInt(nivelMinInput) - 1;
            Nivel nivelMin = niveles.get(nivelMinIdx);
            
            System.out.println("Nivel máximo:");
            for (int i = 0; i < niveles.size(); i++) {
                System.out.println((i + 1) + ". " + niveles.get(i).getNombre());
            }
            System.out.print("Seleccione [default: 3]: ");
            String nivelMaxInput = scanner.nextLine().trim();
            int nivelMaxIdx = nivelMaxInput.isEmpty() ? 2 : Integer.parseInt(nivelMaxInput) - 1;
            Nivel nivelMax = niveles.get(nivelMaxIdx);
            
            Partido partido = partidoCreacionController.crearPartido(
                deporte, usuarioActual, jugadoresRequeridos, ubicacion, fechaHora, duracion, nivelMin, nivelMax
            );
            
            if (partido != null) {
                System.out.println("✓ Partido creado exitosamente (ID: " + partido.getId() + ")");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al crear partido: " + e.getMessage());
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

