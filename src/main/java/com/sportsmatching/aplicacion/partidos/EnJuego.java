package com.sportsmatching.aplicacion.partidos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.aplicacion.partidos.Finalizado;

import java.time.LocalDateTime;

public class EnJuego implements PartidoState {
    @Override
    public void onJugadorAgregado(Partido partido) {
        // No se pueden agregar jugadores durante el partido
    }

    @Override
    public void onJugadorRemovido(Partido partido) {
        // No se pueden remover jugadores durante el partido
    }

    @Override
    public void onTiempoComienzo(Partido partido) {
        // Ya está en juego
    }

    @Override
    public void onTiempoFinalizacion(Partido partido) {
        LocalDateTime fin = partido.getFechaHora().plusMinutes(partido.getDuracion());
        if (fin.isBefore(LocalDateTime.now()) || fin.isEqual(LocalDateTime.now())) {
            partido.setEstado(new Finalizado());
        }
    }

    @Override
    public void confirmar(Partido partido) {
        // No aplica
    }

    @Override
    public void cancelar(Partido partido) {
        // No se puede cancelar durante el partido
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
        return "En Juego";
    }
}

