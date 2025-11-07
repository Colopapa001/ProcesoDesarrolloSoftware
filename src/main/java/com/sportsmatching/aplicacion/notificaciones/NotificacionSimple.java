package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.aplicacion.notificaciones.Observer;

public class NotificacionSimple extends ComponenteNotificacion {
    private final Observer observer;

    public NotificacionSimple(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void enviar(String evento, Partido partido) {
        observer.update(evento, partido);
    }
}

