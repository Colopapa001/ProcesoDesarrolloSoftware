package com.sportsmatching.mvc.catalogos;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;

import java.util.List;

public class CatalogoView {
    public void mostrarDeportes(List<Deporte> deportes) {
        System.out.println("=== Deportes ===");
        deportes.forEach(d -> System.out.println("- " + d.getNombre()));
    }

    public void mostrarNiveles(List<Nivel> niveles) {
        System.out.println("=== Niveles ===");
        niveles.forEach(n -> System.out.println("- " + n.getNombre()));
    }
}

