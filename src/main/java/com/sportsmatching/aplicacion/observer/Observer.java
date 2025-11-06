package com.sportsmatching.aplicacion.observer;

import com.sportsmatching.dominio.Partido;

public interface Observer {
    void update(String evento, Partido partido);
}

