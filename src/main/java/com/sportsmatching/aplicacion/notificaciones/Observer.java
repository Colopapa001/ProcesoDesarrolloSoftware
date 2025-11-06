package com.sportsmatching.aplicacion.notificaciones;

import com.sportsmatching.dominio.Partido;

public interface Observer {
    void update(String evento, Partido partido);
}

