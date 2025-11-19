package com.sportsmatching;

import com.sportsmatching.infraestructura.notification.FirebasePushAdapter;
import com.sportsmatching.infraestructura.notification.JavaMailAdapter;
import com.sportsmatching.dominio.PartidoValidacion;
import com.sportsmatching.presentacion.mvc.autenticacion.AuthController;
import com.sportsmatching.presentacion.mvc.autenticacion.modelos.AuthModel;
import com.sportsmatching.presentacion.mvc.autenticacion.AuthView;
import com.sportsmatching.presentacion.mvc.autenticacion.servicios.AutenticacionService;
import com.sportsmatching.presentacion.mvc.busqueda.BusquedaController;
import com.sportsmatching.presentacion.mvc.busqueda.servicios.BusquedaFiltroService;
import com.sportsmatching.presentacion.mvc.busqueda.BusquedaView;
import com.sportsmatching.presentacion.mvc.busqueda.modelos.BusquedaModel;
import com.sportsmatching.presentacion.mvc.catalogos.CatalogoController;
import com.sportsmatching.presentacion.mvc.catalogos.CatalogoView;
import com.sportsmatching.presentacion.mvc.catalogos.modelos.CatalogoModel;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoCreacionController;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoEstadisticasController;
import com.sportsmatching.presentacion.mvc.partido.controladores.PartidoGestionController;
import com.sportsmatching.presentacion.mvc.partido.modelos.DefaultPartidoNotificacionService;
import com.sportsmatching.presentacion.mvc.partido.modelos.HtmlPartidoEmailBuilder;
import com.sportsmatching.presentacion.mvc.partido.modelos.NearbyInterestedRecipientSelector;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoEmailBuilder;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoNotificacionService;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRecipientSelector;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRepository;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoService;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoDetailView;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoFormView;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoListView;
import com.sportsmatching.presentacion.mvc.registro.RegistroController;
import com.sportsmatching.presentacion.mvc.registro.modelos.RegistroModel;
import com.sportsmatching.presentacion.mvc.registro.RegistroView;
import com.sportsmatching.presentacion.mvc.registro.servicios.UsuarioValidacionService;
import com.sportsmatching.aplicacion.notificaciones.EmailObserver;
import com.sportsmatching.aplicacion.notificaciones.NotificacionSubject;
import com.sportsmatching.aplicacion.notificaciones.PushObserver;
import com.sportsmatching.infraestructura.persistence.CatalogoRepository;
import com.sportsmatching.infraestructura.persistence.InMemoryCatalogoRepository;
import com.sportsmatching.infraestructura.persistence.InMemoryPartidoRepository;
import com.sportsmatching.infraestructura.persistence.InMemoryUsuarioRepository;
import com.sportsmatching.infraestructura.persistence.UsuarioRepository;
import com.sportsmatching.aplicacion.emparejamiento.EmparejamientoPorNivel;
import com.sportsmatching.aplicacion.emparejamiento.MatchmakingService;
import com.sportsmatching.aplicacion.servicios.DistanceCalculator;
import com.sportsmatching.aplicacion.servicios.DistanciaService;
import com.sportsmatching.presentacion.view.InteractiveMenu;
import com.sportsmatching.infra.DataSeeder;

public class Main {
    public static void main(String[] args) {
        System.out.println("Inicializando sistema...");

        // Configurar valores por defecto para email si no están establecidos
        if (System.getProperty("email.provider") == null) {
            System.setProperty("email.provider", "gmail");
        }
        if (System.getProperty("email.username") == null) {
            System.setProperty("email.username", "ji.papa.colo@gmail.com");
        }
        if (System.getProperty("email.password") == null) {
            System.setProperty("email.password", "nhvq vqtj okqa twmy");
        }

        // Repositorios
        UsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();
        // Crear primero el repositorio MVC y luego el adaptador para que compartan los mismos datos
        com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRepository partidoRepositoryMVC = new com.sportsmatching.presentacion.mvc.partido.modelos.InMemoryPartidoRepository();
        com.sportsmatching.infraestructura.persistence.PartidoRepository partidoRepository = new InMemoryPartidoRepository(partidoRepositoryMVC);
        CatalogoRepository catalogoRepository = new InMemoryCatalogoRepository();

        // Cargar datos iniciales (mock data)
        DataSeeder.seed();
        // Cargar datos del mock a los repositorios reales
        DataSeeder.cargarEnRepositorios(usuarioRepository, partidoRepositoryMVC, catalogoRepository);

        // Strategy
        MatchmakingService matchmakingService = new MatchmakingService(new EmparejamientoPorNivel());

        // Observer + Adapter
        NotificacionSubject notificacionSubject = new NotificacionSubject();
        JavaMailAdapter javaMailAdapter = new JavaMailAdapter();
        FirebasePushAdapter firebasePushAdapter = new FirebasePushAdapter();
        EmailObserver emailObserver = new EmailObserver(javaMailAdapter);
        PushObserver pushObserver = new PushObserver(firebasePushAdapter);
        notificacionSubject.attach(emailObserver);
        notificacionSubject.attach(pushObserver);

        // Validación
        PartidoValidacion partidoValidacion = new PartidoValidacion();

        // Services
        DistanceCalculator distanceCalculator = new DistanciaService();
        PartidoRecipientSelector recipientSelector = new NearbyInterestedRecipientSelector(usuarioRepository, distanceCalculator, 50.0);
        PartidoEmailBuilder partidoEmailBuilder = new HtmlPartidoEmailBuilder();
        PartidoNotificacionService partidoNotificacionService = new DefaultPartidoNotificacionService(
            notificacionSubject,
            recipientSelector,
            partidoEmailBuilder,
            javaMailAdapter
        );
        PartidoService partidoService = new PartidoService(partidoValidacion, matchmakingService, partidoNotificacionService);

        // MVC - Registro
        UsuarioValidacionService usuarioValidacionService = new UsuarioValidacionService(usuarioRepository);
        RegistroModel registroModel = new RegistroModel(usuarioValidacionService, usuarioRepository);
        RegistroView registroView = new RegistroView();
        RegistroController registroController = new RegistroController(registroModel, registroView);

        // MVC - Autenticación
        AutenticacionService autenticacionService = new AutenticacionService(usuarioRepository);
        AuthModel authModel = new AuthModel(autenticacionService);
        AuthView authView = new AuthView();
        AuthController authController = new AuthController(authModel, authView);

        // MVC - Búsqueda
        BusquedaFiltroService busquedaFiltroService = new BusquedaFiltroService(distanceCalculator);
        BusquedaModel busquedaModel = new BusquedaModel(partidoRepository, busquedaFiltroService);
        BusquedaView busquedaView = new BusquedaView();
        BusquedaController busquedaController = new BusquedaController(busquedaModel, busquedaView);

        // MVC - Catálogos
        CatalogoModel catalogoModel = new CatalogoModel(catalogoRepository);
        CatalogoView catalogoView = new CatalogoView();
        CatalogoController catalogoController = new CatalogoController(catalogoModel, catalogoView);

        // MVC - Partido
        PartidoModel partidoModel = new PartidoModel(partidoRepositoryMVC, partidoService, partidoNotificacionService);
        PartidoListView partidoListView = new PartidoListView();
        PartidoDetailView partidoDetailView = new PartidoDetailView();
        PartidoFormView partidoFormView = new PartidoFormView();
        PartidoCreacionController partidoCreacionController = new PartidoCreacionController(partidoModel, partidoListView);
        PartidoGestionController partidoGestionController = new PartidoGestionController(partidoModel, partidoDetailView, notificacionSubject);
        PartidoEstadisticasController partidoEstadisticasController = new PartidoEstadisticasController(partidoModel, partidoFormView);

        // Interactive Menu
        InteractiveMenu menu = new InteractiveMenu(
            registroController, registroView,
            authController, authView,
            busquedaController, busquedaView,
            catalogoController, catalogoView,
            partidoCreacionController,
            partidoGestionController,
            partidoEstadisticasController,
            partidoListView,
            partidoDetailView,
            partidoFormView,
            partidoModel,
            distanceCalculator
        );

        // Mostrar configuración de email
        String emailUser = System.getProperty("email.username");
        String emailProvider = System.getProperty("email.provider", "outlook");
        
        if (emailUser == null || emailUser.isEmpty()) {
            System.out.println("⚠ Email no configurado - Los emails se guardarán en archivos");
        } else {
            System.out.println("✓ Email configurado: " + emailUser + " (" + emailProvider + ")");
        }
        
        System.out.println("\n");

        // Iniciar menú interactivo
        menu.run();
    }
}

