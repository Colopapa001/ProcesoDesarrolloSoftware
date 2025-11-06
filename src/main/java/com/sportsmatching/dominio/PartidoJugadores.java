package com.sportsmatching.dominio;

import java.util.ArrayList;
import java.util.List;

public class PartidoJugadores {
    private List<Usuario> jugadores;
    private int jugadoresRequeridos;

    public PartidoJugadores(int jugadoresRequeridos) {
        this.jugadores = new ArrayList<>();
        this.jugadoresRequeridos = jugadoresRequeridos;
    }

    public boolean agregarJugador(Usuario usuario) {
        if (puedeAgregarJugador(usuario)) {
            jugadores.add(usuario);
            return true;
        }
        return false;
    }

    public boolean removerJugador(Usuario usuario) {
        return jugadores.remove(usuario);
    }

    public boolean verificarCompletitud() {
        return jugadores.size() >= jugadoresRequeridos;
    }

    public boolean puedeAgregarJugador(Usuario usuario) {
        return !jugadores.contains(usuario) && jugadores.size() < jugadoresRequeridos;
    }

    public int obtenerCantidadDisponible() {
        return Math.max(0, jugadoresRequeridos - jugadores.size());
    }

    public List<Usuario> getJugadores() { return new ArrayList<>(jugadores); }
    public int getJugadoresRequeridos() { return jugadoresRequeridos; }
}

