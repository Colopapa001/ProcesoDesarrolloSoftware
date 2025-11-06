package com.sportsmatching.observer;

import com.sportsmatching.adapter.NotificationClient;
import com.sportsmatching.dominio.Partido;

public class PushObserver implements Observer {
    private final NotificationClient client;

    public PushObserver(NotificationClient client) {
        this.client = client;
    }

    @Override
    public void update(String evento, Partido partido) {
        String mensaje = construirMensaje(evento, partido);
        String asunto = "Notificación Push: " + evento;
        // Para push, el destino sería un token, pero usamos email como placeholder
        client.send(partido.getOrganizador().getEmail(), asunto, mensaje);
    }

    public String construirMensaje(String evento, Partido partido) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento: ").append(evento).append("\n");
        sb.append("Partido: ").append(partido.getDeporte().getNombre()).append("\n");
        return sb.toString();
    }
}

