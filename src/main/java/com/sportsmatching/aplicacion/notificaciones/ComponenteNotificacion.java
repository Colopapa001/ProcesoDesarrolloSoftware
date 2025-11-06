package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.dominio.Partido;

import java.util.List;

public abstract class ComponenteNotificacion {
    public abstract void enviar(String evento, Partido partido);
    
    public void agregar(ComponenteNotificacion componente) {
        throw new UnsupportedOperationException();
    }
    
    public void remover(ComponenteNotificacion componente) {
        throw new UnsupportedOperationException();
    }
    
    public List<ComponenteNotificacion> obtenerHijos() {
        throw new UnsupportedOperationException();
    }
}

