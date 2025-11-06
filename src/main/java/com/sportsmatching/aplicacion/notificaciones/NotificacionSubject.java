package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.dominio.Partido;

import java.util.ArrayList;
import java.util.List;

public class NotificacionSubject {
    private List<Observer> observers;

    public NotificacionSubject() {
        this.observers = new ArrayList<>();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notify(String evento, Partido partido) {
        observers.forEach(o -> o.update(evento, partido));
    }
}

