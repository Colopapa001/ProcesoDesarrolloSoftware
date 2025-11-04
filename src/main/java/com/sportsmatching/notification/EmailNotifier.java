package com.sportsmatching.notification;

import com.sportsmatching.model.Match;
import com.sportsmatching.model.User;
import com.sportsmatching.service.EmailService;

import java.time.format.DateTimeFormatter;

public class EmailNotifier implements Notifier {
    private final EmailService emailService;

    public EmailNotifier() {
        this.emailService = new EmailService();
    }

    @Override
    public void notify(User user, Match match, MatchEvent event) {
        String subject = getSubject(event);
        String body = getEmailBody(user, match, event);
        
        boolean sent = emailService.sendEmail(user.getEmail(), subject, body);
        if (sent) {
            System.out.println("    ✓ Email enviado a " + user.getEmail());
        } else {
            System.out.println("    ⚠ Email no enviado (configura SMTP en variables de sistema)");
        }
    }

    private String getSubject(MatchEvent event) {
        return switch (event.getName()) {
            case "NEW_FOR_FAVORITE_SPORT" -> "⚽ Nuevo partido disponible para tu deporte favorito";
            case "ASSEMBLED" -> "✓ Partido completo - Todos los jugadores unidos";
            case "CONFIRMED" -> "✓ Partido confirmado - Listo para jugar";
            case "IN_PROGRESS" -> "⚽ Partido en curso";
            case "FINISHED" -> "🏁 Partido finalizado";
            case "CANCELED" -> "❌ Partido cancelado";
            default -> "Notificación de partido";
        };
    }

    private String getEmailBody(User user, Match match, MatchEvent event) {
        StringBuilder body = new StringBuilder();
        body.append("Hola ").append(user.getUsername()).append(",\n\n");
        
        switch (event.getName()) {
            case "NEW_FOR_FAVORITE_SPORT":
                body.append("¡Hay un nuevo partido disponible para tu deporte favorito!\n\n");
                break;
            case "ASSEMBLED":
                body.append("¡Excelente! El partido ahora tiene todos los jugadores necesarios.\n\n");
                break;
            case "CONFIRMED":
                body.append("El partido ha sido confirmado y está listo para jugarse.\n\n");
                break;
            case "IN_PROGRESS":
                body.append("El partido ha comenzado. ¡Disfruta del juego!\n\n");
                break;
            case "FINISHED":
                body.append("El partido ha finalizado. ¡Gracias por participar!\n\n");
                break;
            case "CANCELED":
                body.append("El partido ha sido cancelado.\n\n");
                break;
        }
        
        body.append("Detalles del partido:\n");
        body.append("- Deporte: ").append(match.getSportType().getName()).append("\n");
        body.append("- Ubicación: ").append(match.getLocationDescription()).append("\n");
        body.append("- Coordenadas: ").append(match.getLocation()).append("\n");
        body.append("- Fecha y hora: ").append(match.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        body.append("- Jugadores requeridos: ").append(match.getRequiredPlayers()).append("\n");
        body.append("- Jugadores actuales: ").append(match.getPlayers().size()).append("\n");
        body.append("- Estado: ").append(match.getState().name()).append("\n\n");
        
        if (event.getName().equals("NEW_FOR_FAVORITE_SPORT")) {
            body.append("¡Únete al partido ahora!\n");
        }
        
        body.append("Saludos,\nSistema de Gestión de Partidos Deportivos");
        
        return body.toString();
    }
}


