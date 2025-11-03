package com.sportsmatching.notification;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;

public class EmailNotifier implements Notifier {
    @Override
    public void notify(User user, Match match, MatchEvent event) {
        // Adapter to JavaMail would go here. For now, we use System.out for cleaner output.
        String eventMsg = switch (event.getName()) {
            case "NEW_FOR_FAVORITE_SPORT" -> "Nuevo partido disponible";
            case "ASSEMBLED" -> "Partido completo";
            case "CONFIRMED" -> "Partido confirmado";
            case "IN_PROGRESS" -> "Partido en curso";
            case "FINISHED" -> "Partido finalizado";
            case "CANCELED" -> "Partido cancelado";
            default -> event.getName();
        };
        System.out.println("    📧 Email enviado a " + user.getEmail() + ": " + eventMsg);
    }
}

