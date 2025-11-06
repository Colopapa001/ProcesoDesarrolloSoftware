package com.sportsmatching.aplicacion.state;

import com.sportsmatching.dominio.Partido;

public class PartidoArmado implements PartidoState {
    @Override
    public void onJugadorAgregado(Partido partido) {
        // Ya está completo
    }

    @Override
    public void onJugadorRemovido(Partido partido) {
        if (!partido.getPartidoJugadores().verificarCompletitud()) {
            partido.setEstado(new NecesitamosJugadores());
        }
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
        if (puedeConfirmar(partido)) {
            partido.setEstado(new Confirmado());
        }
    }

    @Override
    public void cancelar(Partido partido) {
        partido.setEstado(new Cancelado());
    }

    @Override
    public boolean puedeAgregarJugador(Partido partido) {
        return false;
    }

    @Override
    public boolean puedeConfirmar(Partido partido) {
        return partido.getPartidoJugadores().verificarCompletitud();
    }

    @Override
    public boolean puedeCancelar(Partido partido) {
        return true;
    }

    @Override
    public String getNombreEstado() {
        return "Partido Armado";
    }
}

