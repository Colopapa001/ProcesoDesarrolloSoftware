package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.infraestructura.notification.NotificationClient;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailObserver implements Observer {
    private final NotificationClient client;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EmailObserver(NotificationClient client) {
        this.client = client;
    }

    @Override
    public void update(String evento, Partido partido) {
        String mensaje = construirMensaje(evento, partido);
        String asunto = construirAsunto(evento, partido);
        
        // Enviar al organizador
        client.send(partido.getOrganizador().getEmail(), asunto, mensaje);
        
        // Enviar a todos los jugadores inscritos
        List<Usuario> jugadores = partido.getPartidoJugadores().getJugadores();
        for (Usuario jugador : jugadores) {
            if (!jugador.getId().equals(partido.getOrganizador().getId())) {
                client.send(jugador.getEmail(), asunto, mensaje);
            }
        }
    }

    private String construirAsunto(String evento, Partido partido) {
        switch (evento) {
            case "CREACION":
                return "🎉 ¡Partido creado exitosamente! - " + partido.getDeporte().getNombre();
            case "CAMBIO_ESTADO":
                String estado = partido.getEstado().getNombreEstado();
                if (estado.equals("Partido Armado")) {
                    return "✅ ¡Partido completo! - " + partido.getDeporte().getNombre();
                } else if (estado.equals("Confirmado")) {
                    return "🎯 Partido confirmado - " + partido.getDeporte().getNombre();
                } else if (estado.equals("En Juego")) {
                    return "⚽ ¡El partido ha comenzado! - " + partido.getDeporte().getNombre();
                } else if (estado.equals("Finalizado")) {
                    return "🏁 Partido finalizado - " + partido.getDeporte().getNombre();
                } else if (estado.equals("Cancelado")) {
                    return "❌ Partido cancelado - " + partido.getDeporte().getNombre();
                }
                return "📢 Actualización del partido - " + partido.getDeporte().getNombre();
            default:
                return "Notificación de Partido - " + partido.getDeporte().getNombre();
        }
    }

    public String construirMensaje(String evento, Partido partido) {
        if (evento.equals("CREACION")) {
            return construirMensajeCreacion(partido);
        } else if (evento.equals("CAMBIO_ESTADO")) {
            return construirMensajeCambioEstado(partido);
        } else {
            return construirMensajeGenerico(evento, partido);
        }
    }

    private String construirMensajeCreacion(Partido partido) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head><meta charset='UTF-8'></head>\n<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>\n");
        html.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;'>\n");
        
        // Header
        html.append("<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>\n");
        html.append("<h1 style='margin: 0; font-size: 28px;'>🎉 ¡Partido Creado!</h1>\n");
        html.append("<p style='margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;'>Tu partido ha sido creado exitosamente</p>\n");
        html.append("</div>\n");
        
        // Content
        html.append("<div style='background: white; padding: 30px; border-radius: 0 0 10px 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n");
        
        html.append("<h2 style='color: #667eea; margin-top: 0;'>Detalles del Partido</h2>\n");
        html.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; width: 40%; color: #555;'>Deporte:</td><td style='padding: 10px;'>").append(partido.getDeporte().getNombre()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Fecha y Hora:</td><td style='padding: 10px;'>").append(partido.getFechaHora().format(DATE_FORMATTER)).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Duración:</td><td style='padding: 10px;'>").append(partido.getDuracion()).append(" minutos</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Ubicación:</td><td style='padding: 10px;'>").append(partido.getUbicacion().getDescripcion()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Jugadores Requeridos:</td><td style='padding: 10px;'>").append(partido.getJugadoresRequeridos()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Nivel:</td><td style='padding: 10px;'>").append(partido.getNivelMin().getNombre()).append(" - ").append(partido.getNivelMax().getNombre()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Estado Actual:</td><td style='padding: 10px;'><span style='background-color: #fff3cd; color: #856404; padding: 5px 10px; border-radius: 5px; font-weight: bold;'>").append(partido.getEstado().getNombreEstado()).append("</span></td></tr>\n");
        html.append("</table>\n");
        
        html.append("<div style='background-color: #e7f3ff; border-left: 4px solid #2196F3; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append("<p style='margin: 0; color: #1976D2;'><strong>📌 Próximos pasos:</strong></p>\n");
        html.append("<ul style='margin: 10px 0 0 20px; color: #1976D2;'>\n");
        html.append("<li>Comparte el partido para que otros jugadores se unan</li>\n");
        html.append("<li>Revisa las inscripciones regularmente</li>\n");
        html.append("<li>Cuando el partido esté completo, confírmalo para proceder</li>\n");
        html.append("</ul>\n");
        html.append("</div>\n");
        
        html.append("<p style='text-align: center; margin-top: 30px; color: #666;'>¡Gracias por usar nuestra plataforma!</p>\n");
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");
        
        return html.toString();
    }

    private String construirMensajeCambioEstado(Partido partido) {
        String estado = partido.getEstado().getNombreEstado();
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head><meta charset='UTF-8'></head>\n<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>\n");
        html.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;'>\n");
        
        // Header según el estado
        String headerColor = "";
        String headerText = "";
        String headerEmoji = "";
        
        switch (estado) {
            case "Partido Armado":
                headerColor = "background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);";
                headerText = "✅ ¡Partido Completo!";
                headerEmoji = "🎉";
                break;
            case "Confirmado":
                headerColor = "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);";
                headerText = "🎯 Partido Confirmado";
                headerEmoji = "✅";
                break;
            case "En Juego":
                headerColor = "background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);";
                headerText = "⚽ ¡El Partido ha Comenzado!";
                headerEmoji = "🏃";
                break;
            case "Finalizado":
                headerColor = "background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);";
                headerText = "🏁 Partido Finalizado";
                headerEmoji = "🎊";
                break;
            case "Cancelado":
                headerColor = "background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);";
                headerText = "❌ Partido Cancelado";
                headerEmoji = "⚠️";
                break;
            default:
                headerColor = "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);";
                headerText = "📢 Actualización del Partido";
                headerEmoji = "📝";
        }
        
        html.append("<div style='").append(headerColor).append(" color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>\n");
        html.append("<h1 style='margin: 0; font-size: 28px;'>").append(headerEmoji).append(" ").append(headerText).append("</h1>\n");
        html.append("</div>\n");
        
        // Content
        html.append("<div style='background: white; padding: 30px; border-radius: 0 0 10px 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n");
        
        html.append("<h2 style='color: #667eea; margin-top: 0;'>Información del Partido</h2>\n");
        html.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; width: 40%; color: #555;'>Deporte:</td><td style='padding: 10px;'>").append(partido.getDeporte().getNombre()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Fecha y Hora:</td><td style='padding: 10px;'>").append(partido.getFechaHora().format(DATE_FORMATTER)).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Ubicación:</td><td style='padding: 10px;'>").append(partido.getUbicacion().getDescripcion()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Jugadores Inscritos:</td><td style='padding: 10px;'>").append(partido.getPartidoJugadores().getJugadores().size()).append(" / ").append(partido.getJugadoresRequeridos()).append("</td></tr>\n");
        html.append("</table>\n");
        
        // Mensaje específico según el estado
        String mensajeEstado = obtenerMensajeEstado(estado, partido);
        html.append("<div style='background-color: #f0f7ff; border-left: 4px solid #2196F3; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append(mensajeEstado);
        html.append("</div>\n");
        
        // Lista de jugadores inscritos (si aplica)
        if (!estado.equals("Cancelado") && !partido.getPartidoJugadores().getJugadores().isEmpty()) {
            html.append("<div style='margin: 20px 0;'>\n");
            html.append("<h3 style='color: #667eea;'>👥 Jugadores Inscritos:</h3>\n");
            html.append("<ul style='list-style: none; padding: 0;'>\n");
            for (Usuario jugador : partido.getPartidoJugadores().getJugadores()) {
                html.append("<li style='padding: 8px; background-color: #f9f9f9; margin: 5px 0; border-radius: 5px;'>");
                html.append("👤 ").append(jugador.getUsername());
                if (jugador.getId().equals(partido.getOrganizador().getId())) {
                    html.append(" <span style='background-color: #ffd700; color: #333; padding: 2px 8px; border-radius: 3px; font-size: 12px; font-weight: bold;'>(Organizador)</span>");
                }
                html.append("</li>\n");
            }
            html.append("</ul>\n");
            html.append("</div>\n");
        }
        
        html.append("<p style='text-align: center; margin-top: 30px; color: #666;'>¡Gracias por usar nuestra plataforma!</p>\n");
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");
        
        return html.toString();
    }

    private String obtenerMensajeEstado(String estado, Partido partido) {
        switch (estado) {
            case "Partido Armado":
                return "<p style='margin: 0; color: #1976D2;'><strong>🎉 ¡Excelente noticia!</strong><br>" +
                       "Tu partido de " + partido.getDeporte().getNombre() + " ahora está completo con todos los jugadores requeridos. " +
                       "Puedes confirmar el partido cuando estés listo para comenzar.</p>";
            case "Confirmado":
                return "<p style='margin: 0; color: #1976D2;'><strong>✅ Partido Confirmado</strong><br>" +
                       "El partido ha sido confirmado y está listo para jugarse. " +
                       "Recuerda llegar a tiempo a " + partido.getUbicacion().getDescripcion() + " el " + 
                       partido.getFechaHora().format(DATE_FORMATTER) + ".</p>";
            case "En Juego":
                return "<p style='margin: 0; color: #d32f2f;'><strong>⚽ ¡El Partido ha Comenzado!</strong><br>" +
                       "¡Es hora de jugar! El partido de " + partido.getDeporte().getNombre() + " está en curso. " +
                       "¡Disfruta y buena suerte!</p>";
            case "Finalizado":
                return "<p style='margin: 0; color: #1976D2;'><strong>🏁 Partido Finalizado</strong><br>" +
                       "El partido ha finalizado. Gracias por participar. " +
                       "No olvides agregar estadísticas y comentarios sobre cómo fue el partido.</p>";
            case "Cancelado":
                return "<p style='margin: 0; color: #d32f2f;'><strong>❌ Partido Cancelado</strong><br>" +
                       "Lamentamos informarte que el partido ha sido cancelado. " +
                       "Esperamos verte en futuros partidos.</p>";
            default:
                return "<p style='margin: 0; color: #1976D2;'>El estado del partido ha cambiado a: <strong>" + estado + "</strong></p>";
        }
    }

    private String construirMensajeGenerico(String evento, Partido partido) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head><meta charset='UTF-8'></head>\n<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>\n");
        html.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;'>\n");
        html.append("<div style='background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n");
        html.append("<h2 style='color: #667eea;'>Notificación de Partido</h2>\n");
        html.append("<p><strong>Evento:</strong> ").append(evento).append("</p>\n");
        html.append("<p><strong>Deporte:</strong> ").append(partido.getDeporte().getNombre()).append("</p>\n");
        html.append("<p><strong>Ubicación:</strong> ").append(partido.getUbicacion().getDescripcion()).append("</p>\n");
        html.append("<p><strong>Estado:</strong> ").append(partido.getEstado().getNombreEstado()).append("</p>\n");
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");
        return html.toString();
    }
}

