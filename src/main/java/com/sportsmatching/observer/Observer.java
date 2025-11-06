package com.sportsmatching.observer;

import com.sportsmatching.dominio.Partido;

public interface Observer {
    void update(String evento, Partido partido);
}

