package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;

import java.util.List;

public interface CatalogoRepository {
    List<Deporte> obtenerDeportes();
    List<Nivel> obtenerNiveles();
}

