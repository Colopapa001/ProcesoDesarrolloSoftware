package com.sportsmatching.adapter;

public class FirebasePushAdapter implements NotificationClient {
    @Override
    public void send(String destino, String asunto, String cuerpo) {
        // Lógica de push no implementada según instrucciones del usuario
        System.out.println("Push notification (no implementado): " + destino + " - " + asunto);
    }
}

