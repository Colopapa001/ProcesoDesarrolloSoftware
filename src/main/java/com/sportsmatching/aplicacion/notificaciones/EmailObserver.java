package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.infraestructura.notification.NotificationClient;
import com.sportsmatching.dominio.Partido;

public class EmailObserver implements Observer {
    private final NotificationClient client;

    public EmailObserver(NotificationClient client) {
        this.client = client;
    }

    @Override
    public void update(String evento, Partido partido) {
        String mensaje = construirMensaje(evento, partido);
        String asunto = "Notificación de Partido: " + evento;
        client.send(partido.getOrganizador().getEmail(), asunto, mensaje);
    }

    public String construirMensaje(String evento, Partido partido) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento: ").append(evento).append("\n");
        sb.append("Partido: ").append(partido.getDeporte().getNombre()).append("\n");
        sb.append("Ubicación: ").append(partido.getUbicacion()).append("\n");
        sb.append("Estado: ").append(partido.getEstado().getNombreEstado()).append("\n");
        return sb.toString();
    }
}

