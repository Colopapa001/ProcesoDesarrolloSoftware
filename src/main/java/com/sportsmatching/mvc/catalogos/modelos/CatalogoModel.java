package com.sportsmatching.mvc.catalogos.modelos;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;

import java.util.List;

public class CatalogoModel {
    private final com.sportsmatching.repository.CatalogoRepository catalogoRepository;

    public CatalogoModel(com.sportsmatching.repository.CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    public List<Deporte> obtenerDeportes() {
        return catalogoRepository.obtenerDeportes();
    }

    public List<Nivel> obtenerNiveles() {
        return catalogoRepository.obtenerNiveles();
    }
}

