package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.infraestructura.notification.NotificationClient;

public class NotificacionServiceBase implements INotificacionService {
    private final NotificationClient client;

    public NotificacionServiceBase(NotificationClient client) {
        this.client = client;
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        client.send(destino, asunto, cuerpo);
    }
}

