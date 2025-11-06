package com.sportsmatching.aplicacion.notificaciones;

public interface INotificacionService {
    void enviar(String destino, String asunto, String cuerpo);
}

