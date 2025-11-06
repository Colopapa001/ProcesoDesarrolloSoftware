package com.sportsmatching.repository;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCatalogoRepository implements CatalogoRepository {
    private final List<Deporte> deportes;
    private final List<Nivel> niveles;

    public InMemoryCatalogoRepository() {
        this.deportes = new ArrayList<>();
        this.niveles = new ArrayList<>();
        
        // Datos iniciales
        deportes.add(new Deporte(1L, "Fútbol"));
        deportes.add(new Deporte(2L, "Básquet"));
        deportes.add(new Deporte(3L, "Tenis"));
        
        niveles.add(new Nivel(1L, "Principiante"));
        niveles.add(new Nivel(2L, "Intermedio"));
        niveles.add(new Nivel(3L, "Avanzado"));
    }

    @Override
    public List<Deporte> obtenerDeportes() {
        return new ArrayList<>(deportes);
    }

    @Override
    public List<Nivel> obtenerNiveles() {
        return new ArrayList<>(niveles);
    }
}

