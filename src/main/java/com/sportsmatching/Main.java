package com.sportsmatching;

import com.sportsmatching.adapter.FirebasePushAdapter;
import com.sportsmatching.adapter.JavaMailAdapter;
import com.sportsmatching.dominio.PartidoValidacion;
import com.sportsmatching.mvc.autenticacion.AuthController;
import com.sportsmatching.mvc.autenticacion.modelos.AuthModel;
import com.sportsmatching.mvc.autenticacion.AuthView;
import com.sportsmatching.mvc.autenticacion.servicios.AutenticacionService;
import com.sportsmatching.mvc.busqueda.BusquedaController;
import com.sportsmatching.mvc.busqueda.servicios.BusquedaFiltroService;
import com.sportsmatching.mvc.busqueda.BusquedaView;
import com.sportsmatching.mvc.busqueda.modelos.BusquedaModel;
import com.sportsmatching.mvc.catalogos.CatalogoController;
import com.sportsmatching.mvc.catalogos.CatalogoView;
import com.sportsmatching.mvc.catalogos.modelos.CatalogoModel;
import com.sportsmatching.mvc.partido.controladores.PartidoCreacionController;
import com.sportsmatching.mvc.partido.controladores.PartidoEstadisticasController;
import com.sportsmatching.mvc.partido.controladores.PartidoGestionController;
import com.sportsmatching.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.mvc.partido.modelos.PartidoNotificacionService;
import com.sportsmatching.mvc.partido.modelos.PartidoRepository;
import com.sportsmatching.mvc.partido.modelos.PartidoService;
import com.sportsmatching.mvc.partido.vistas.PartidoDetailView;
import com.sportsmatching.mvc.partido.vistas.PartidoFormView;
import com.sportsmatching.mvc.partido.vistas.PartidoListView;
import com.sportsmatching.mvc.registro.RegistroController;
import com.sportsmatching.mvc.registro.modelos.RegistroModel;
import com.sportsmatching.mvc.registro.RegistroView;
import com.sportsmatching.mvc.registro.servicios.UsuarioValidacionService;
import com.sportsmatching.observer.EmailObserver;
import com.sportsmatching.observer.NotificacionSubject;
import com.sportsmatching.observer.PushObserver;
import com.sportsmatching.repository.CatalogoRepository;
import com.sportsmatching.repository.InMemoryCatalogoRepository;
import com.sportsmatching.repository.InMemoryPartidoRepository;
import com.sportsmatching.repository.InMemoryUsuarioRepository;
import com.sportsmatching.repository.UsuarioRepository;
import com.sportsmatching.strategy.EmparejamientoPorNivel;
import com.sportsmatching.strategy.MatchmakingService;
import com.sportsmatching.view.InteractiveMenu;

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
        com.sportsmatching.repository.PartidoRepository partidoRepository = new InMemoryPartidoRepository();
        CatalogoRepository catalogoRepository = new InMemoryCatalogoRepository();
        com.sportsmatching.mvc.partido.modelos.PartidoRepository partidoRepositoryMVC = new com.sportsmatching.mvc.partido.modelos.InMemoryPartidoRepository();

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
        PartidoService partidoService = new PartidoService(partidoValidacion, matchmakingService);
        PartidoNotificacionService partidoNotificacionService = new PartidoNotificacionService(notificacionSubject);

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
        BusquedaFiltroService busquedaFiltroService = new BusquedaFiltroService();
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
            partidoFormView
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

