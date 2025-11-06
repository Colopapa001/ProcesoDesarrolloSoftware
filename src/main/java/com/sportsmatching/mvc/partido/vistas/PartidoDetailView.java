package com.sportsmatching.mvc.partido.vistas;

import com.sportsmatching.dominio.Partido;

public class PartidoDetailView {
    public void mostrarPartido(Partido partido) {
        System.out.println("=== Detalle del Partido ===");
        System.out.println("ID: " + partido.getId());
        System.out.println("Deporte: " + partido.getDeporte().getNombre());
    }

    public void actualizarEstado(String estado) {
        System.out.println("Estado actualizado: " + estado);
    }

    public void actualizarCupos(int disponibles, int requeridos) {
        System.out.println("Cupos disponibles: " + disponibles + " / " + requeridos);
    }
}

