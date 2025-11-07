package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Usuario;

public class PartidoDestinatario {
    private final Usuario usuario;
    private final double distanciaKm;

    public PartidoDestinatario(Usuario usuario, double distanciaKm) {
        this.usuario = usuario;
        this.distanciaKm = distanciaKm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }
}

