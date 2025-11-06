package com.sportsmatching.aplicacion.state;

import com.sportsmatching.dominio.Partido;

public class NecesitamosJugadores implements PartidoState {
    @Override
    public void onJugadorAgregado(Partido partido) {
        if (partido.getPartidoJugadores().verificarCompletitud()) {
            partido.setEstado(new PartidoArmado());
        }
    }

    @Override
    public void onJugadorRemovido(Partido partido) {
        // Ya está en el estado correcto
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
        // No se puede confirmar aún
    }

    @Override
    public void cancelar(Partido partido) {
        partido.setEstado(new Cancelado());
    }

    @Override
    public boolean puedeAgregarJugador(Partido partido) {
        return true;
    }

    @Override
    public boolean puedeConfirmar(Partido partido) {
        return false;
    }

    @Override
    public boolean puedeCancelar(Partido partido) {
        return true;
    }

    @Override
    public String getNombreEstado() {
        return "Necesitamos Jugadores";
    }
}

