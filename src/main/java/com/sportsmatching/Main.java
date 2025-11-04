package com.sportsmatching;

import com.sportsmatching.controller.MatchController;
import com.sportsmatching.controller.UserController;
import com.sportsmatching.repository.InMemoryMatchRepository;
import com.sportsmatching.repository.InMemoryUserRepository;
import com.sportsmatching.service.MatchService;
import com.sportsmatching.service.MatchingService;
import com.sportsmatching.service.SessionService;
import com.sportsmatching.strategy.BySkillLevelStrategy;
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

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryMatchRepository matchRepository = new InMemoryMatchRepository();

        MatchingService matchingService = new MatchingService(new BySkillLevelStrategy());
        MatchService matchService = new MatchService(matchRepository, userRepository, matchingService);

        UserController userController = new UserController(userRepository);
        MatchController matchController = new MatchController(matchService);
        SessionService sessionService = new SessionService(userRepository);

        InteractiveMenu menu = new InteractiveMenu(userController, matchController, sessionService);
        
        // Mostrar configuración de email
        String emailUser = System.getProperty("email.username");
        String emailProvider = System.getProperty("email.provider", "outlook");
        
        if (emailUser == null || emailUser.isEmpty()) {
            System.out.println("\n⚠ ATENCIÓN: Email no configurado.");
            System.out.println("Los emails se guardarán en archivos en la carpeta 'emails/'");
            System.out.println("\nPara enviar emails reales, configura:");
            System.out.println("  -Demail.provider=gmail -Demail.username=tu-email@gmail.com -Demail.password=tu-password");
            System.out.println("  -Demail.provider=outlook -Demail.username=tu-email@outlook.com -Demail.password=tu-password");
            System.out.println("  -Demail.provider=yahoo -Demail.username=tu-email@yahoo.com -Demail.password=tu-password");
            System.out.println("\nNota: Para Gmail necesitas App Password. Para otros proveedores usa tu contraseña normal.\n");
        } else {
            System.out.println("✓ Email configurado. Proveedor: " + emailProvider);
            System.out.println("  Usuario: " + emailUser);
            System.out.println("  Los emails se enviarán por SMTP\n");
        }

        menu.run();
    }
}

