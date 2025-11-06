package com.sportsmatching.aplicacion.partidos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.aplicacion.partidos.EnJuego;

import java.time.LocalDateTime;

public class Confirmado implements PartidoState {
    @Override
    public void onJugadorAgregado(Partido partido) {
        // No se pueden agregar más jugadores
    }

    @Override
    public void onJugadorRemovido(Partido partido) {
        // No se pueden remover jugadores
    }

    @Override
    public void onTiempoComienzo(Partido partido) {
        if (partido.getFechaHora().isBefore(LocalDateTime.now()) || 
            partido.getFechaHora().isEqual(LocalDateTime.now())) {
            partido.setEstado(new EnJuego());
        }
    }

    @Override
    public void onTiempoFinalizacion(Partido partido) {
        // No aplica
    }

    @Override
    public void confirmar(Partido partido) {
        // Ya está confirmado
    }

    @Override
    public void cancelar(Partido partido) {
        if (puedeCancelar(partido)) {
            partido.setEstado(new Cancelado());
        }
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
        return partido.getFechaHora().isAfter(LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "Confirmado";
    }
}

