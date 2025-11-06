package com.sportsmatching.presentacion.mvc.partido.vistas;

import com.sportsmatching.dominio.Partido;

import java.util.List;

public class PartidoListView {
    public void mostrarLista(List<Partido> partidos) {
        System.out.println("=== Lista de Partidos ===");
        partidos.forEach(p -> System.out.println("Partido ID: " + p.getId()));
    }
}

