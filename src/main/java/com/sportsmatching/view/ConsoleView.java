package com.sportsmatching.view;

import com.sportsmatching.controller.MatchController;
import com.sportsmatching.controller.UserController;
import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.Match;
import com.sportsmatching.model.SportType;
import com.sportsmatching.notification.MatchEvent;
import com.sportsmatching.notification.NotificationObserver;

import java.time.LocalDateTime;

public class ConsoleView implements NotificationObserver {
    private final UserController userController;
    private final MatchController matchController;

    public ConsoleView(UserController userController, MatchController matchController) {
        this.userController = userController;
        this.matchController = matchController;
    }

    public void runDemo() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE PARTIDOS DEPORTIVOS ===\n");
        
        Match match = matchController.create(ReferenceData.get().sport("FOOTBALL"), 2, 60, "Parque Central", LocalDateTime.now());
        System.out.println("✓ Partido creado: " + match.getSportType().getName() + 
                          " en " + match.getLocation() + 
                          " (" + match.getRequiredPlayers() + " jugadores necesarios)");
        match.addObserver(this);

        System.out.println("\n→ Ana se une al partido...");
        matchController.join(match.getId(), "ana");
        
        System.out.println("→ Beto se une al partido...");
        matchController.join(match.getId(), "beto");

        System.out.println("\n✓ Todos los jugadores confirmados");
        matchController.confirm(match.getId());
        
        System.out.println("→ Iniciando partido...");
        matchController.startIfTime(match.getId());
        
        System.out.println("→ Partido finalizado\n");
        matchController.finish(match.getId());
    }

    @Override
    public void onMatchEvent(Match match, MatchEvent event) {
        String message = switch (event.getName()) {
            case "NEW_FOR_FAVORITE_SPORT" -> "📧 Nueva partido disponible para tu deporte favorito";
            case "ASSEMBLED" -> "✓ Partido completo - Todos los jugadores unidos";
            case "CONFIRMED" -> "✓ Partido confirmado - Listo para jugar";
            case "IN_PROGRESS" -> "⚽ Partido en curso";
            case "FINISHED" -> "🏁 Partido finalizado";
            case "CANCELED" -> "❌ Partido cancelado";
            default -> "📢 Evento: " + event.getName();
        };
        
        System.out.println("  " + message);
    }
}

