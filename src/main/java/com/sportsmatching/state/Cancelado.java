package com.sportsmatching.state;

import com.sportsmatching.dominio.Partido;

public class Cancelado implements PartidoState {
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
        // No aplica
    }

    @Override
    public void confirmar(Partido partido) {
        // No aplica
    }

    @Override
    public void cancelar(Partido partido) {
        // Ya está cancelado
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
        return "Cancelado";
    }
}

