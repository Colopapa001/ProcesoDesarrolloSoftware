package com.sportsmatching.composite;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.observer.Observer;

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

