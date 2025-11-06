package com.sportsmatching.aplicacion.decorator;

public interface INotificacionService {
    void enviar(String destino, String asunto, String cuerpo);
}

