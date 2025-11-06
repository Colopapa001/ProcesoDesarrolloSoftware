package com.sportsmatching.aplicacion.partidos;

import com.sportsmatching.dominio.Partido;

public interface PartidoState {
    void onJugadorAgregado(Partido partido);
    void onJugadorRemovido(Partido partido);
    void onTiempoComienzo(Partido partido);
    void onTiempoFinalizacion(Partido partido);
    void confirmar(Partido partido);
    void cancelar(Partido partido);
    boolean puedeAgregarJugador(Partido partido);
    boolean puedeConfirmar(Partido partido);
    boolean puedeCancelar(Partido partido);
    String getNombreEstado();
}

