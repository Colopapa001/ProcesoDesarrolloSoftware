package com.sportsmatching.adapter;

public interface NotificationClient {
    void send(String destino, String asunto, String cuerpo);
}

