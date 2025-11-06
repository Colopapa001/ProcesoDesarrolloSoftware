package com.sportsmatching.aplicacion.composite;

import com.sportsmatching.dominio.Partido;

import java.util.ArrayList;
import java.util.List;

public class NotificacionCompuesta extends ComponenteNotificacion {
    private List<ComponenteNotificacion> componentes;

    public NotificacionCompuesta() {
        this.componentes = new ArrayList<>();
    }

    @Override
    public void enviar(String evento, Partido partido) {
        componentes.forEach(c -> c.enviar(evento, partido));
    }

    @Override
    public void agregar(ComponenteNotificacion componente) {
        componentes.add(componente);
    }

    @Override
    public void remover(ComponenteNotificacion componente) {
        componentes.remove(componente);
    }

    @Override
    public List<ComponenteNotificacion> obtenerHijos() {
        return new ArrayList<>(componentes);
    }
}

