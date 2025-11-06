package com.sportsmatching.infraestructura.notification;

public interface NotificationClient {
    void send(String destino, String asunto, String cuerpo);
}

