package com.sportsmatching.aplicacion.partidos;

import com.sportsmatching.dominio.Partido;

public class Finalizado implements PartidoState {
    @Override
    public void onJugadorAgregado(Partido partido) {
        // No aplica
    }

    @Override
    public void onJugadorRemovido(Partido partido) {
        // No aplica
    }

    @Override
    public void onTiempoComienzo(Partido partido) {
        // No aplica
    }

    @Override
    public void onTiempoFinalizacion(Partido partido) {
        // Ya está finalizado
    }

    @Override
    public void confirmar(Partido partido) {
        // No aplica
    }

    @Override
    public void cancelar(Partido partido) {
        // No aplica
    }

    @Override
    public boolean puedeAgregarJugador(Partido partido) {
        return false;
    }

    @Override
    public boolean puedeConfirmar(Partido partido) {
        return false;
    }

    @Override
    public boolean puedeCancelar(Partido partido) {
        return false;
    }

    @Override
    public String getNombreEstado() {
        return "Finalizado";
    }
}

