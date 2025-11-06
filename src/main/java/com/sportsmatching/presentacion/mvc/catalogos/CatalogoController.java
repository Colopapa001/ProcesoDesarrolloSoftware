package com.sportsmatching.presentacion.mvc.catalogos;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.presentacion.mvc.catalogos.modelos.CatalogoModel;

import java.util.List;

public class CatalogoController {
    private final CatalogoModel model;
    private final CatalogoView view;

    public CatalogoController(CatalogoModel model, CatalogoView view) {
        this.model = model;
        this.view = view;
    }

    public List<Deporte> obtenerDeportes() {
        List<Deporte> deportes = model.obtenerDeportes();
        view.mostrarDeportes(deportes);
        return deportes;
    }

    public List<Nivel> obtenerNiveles() {
        List<Nivel> niveles = model.obtenerNiveles();
        view.mostrarNiveles(niveles);
        return niveles;
    }
}

