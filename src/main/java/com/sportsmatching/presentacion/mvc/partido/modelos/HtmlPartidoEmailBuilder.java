package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.time.format.DateTimeFormatter;

public class HtmlPartidoEmailBuilder implements PartidoEmailBuilder {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public EmailContent construirEmailPartidoCercano(Partido partido, Usuario destinatario, double distanciaKm) {
        String subject = "🎯 ¡Nuevo partido de " + partido.getDeporte().getNombre() + " cerca de ti!";
        String body = construirCuerpo(partido, destinatario, distanciaKm);
        return new EmailContent(subject, body);
    }

    private String construirCuerpo(Partido partido, Usuario destinatario, double distanciaKm) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head><meta charset='UTF-8'></head>\n<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>\n");
        html.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;'>\n");

        html.append("<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>\n");
        html.append("<h1 style='margin: 0; font-size: 28px;'>🎯 ¡Partido Cerca de Ti!</h1>\n");
        html.append("<p style='margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;'>Se creó un nuevo partido de tu deporte favorito</p>\n");
        html.append("</div>\n");

        html.append("<div style='background: white; padding: 30px; border-radius: 0 0 10px 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n");
        html.append("<p style='font-size: 18px; color: #333;'>Hola <strong>").append(destinatario.getUsername()).append("</strong>,</p>\n");
        html.append("<p style='color: #555;'>¡Tenemos excelentes noticias! Se acaba de crear un partido de <strong>").append(partido.getDeporte().getNombre()).append("</strong> muy cerca de ti.</p>\n");

        html.append("<h2 style='color: #667eea; margin-top: 30px;'>Detalles del Partido</h2>\n");
        html.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>\n");
        html.append(fila("Deporte:", "<strong>" + partido.getDeporte().getNombre() + "</strong>"));
        html.append(fila("Fecha y Hora:", partido.getFechaHora().format(DATE_FORMATTER)));
        html.append(fila("Duración:", partido.getDuracion() + " minutos"));
        html.append(fila("Ubicación:", partido.getUbicacion().getDescripcion()));
        html.append(fila("📍 Distancia:", "<strong style='color: #2196F3;'>" + String.format("%.2f", distanciaKm) + " km</strong> de ti"));
        html.append(fila("Jugadores Requeridos:", String.valueOf(partido.getJugadoresRequeridos())));
        html.append(fila("Cupos Disponibles:", "<strong style='color: #4CAF50;'>" + partido.getPartidoJugadores().obtenerCantidadDisponible() + "</strong>"));
        html.append(fila("Nivel:", partido.getNivelMin().getNombre() + " - " + partido.getNivelMax().getNombre()));
        html.append(fila("Organizador:", partido.getOrganizador().getUsername()));
        html.append("</table>\n");

        html.append("<div style='background-color: #e8f5e9; border-left: 4px solid #4CAF50; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append("<p style='margin: 0; color: #2e7d32;'><strong>✅ Este partido es perfecto para ti porque:</strong></p>\n");
        html.append("<ul style='margin: 10px 0 0 20px; color: #2e7d32;'>\n");
        html.append("<li>Está a solo ").append(String.format("%.2f", distanciaKm)).append(" km de distancia</li>\n");
        html.append("<li>Es de tu deporte favorito: <strong>").append(partido.getDeporte().getNombre()).append("</strong></li>\n");
        html.append("<li>Tu nivel (").append(destinatario.getNivel().getNombre()).append(") está dentro del rango requerido</li>\n");
        html.append("</ul>\n");
        html.append("</div>\n");

        html.append("<div style='background-color: #fff3e0; border-left: 4px solid #FF9800; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append("<p style='margin: 0; color: #E65100;'><strong>⚡ ¡No esperes más!</strong></p>\n");
        html.append("<p style='margin: 10px 0 0 0; color: #E65100;'>Ingresa a la plataforma y únete al partido antes de que se llene. ¡Los cupos son limitados!</p>\n");
        html.append("</div>\n");

        html.append("<p style='text-align: center; margin-top: 30px; color: #666;'>¡Esperamos verte en el partido!</p>\n");
        html.append("<p style='text-align: center; color: #666;'>El equipo de Sports Matching</p>\n");
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");

        return html.toString();
    }

    private String fila(String titulo, String valor) {
        return "<tr><td style='padding: 10px; font-weight: bold; width: 40%; color: #555;'>" +
                titulo + "</td><td style='padding: 10px;'>" + valor + "</td></tr>\n";
    }
}

