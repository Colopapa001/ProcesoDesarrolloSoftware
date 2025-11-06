package com.sportsmatching.mvc.busqueda;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.mvc.busqueda.modelos.BusquedaModel;

import java.util.List;
import java.util.Map;

public class BusquedaController {
    private final BusquedaModel model;
    private final BusquedaView view;

    public BusquedaController(BusquedaModel model, BusquedaView view) {
        this.model = model;
        this.view = view;
    }

    public List<Partido> buscarPartidos(Map<String, Object> criterios) {
        List<Partido> resultados = model.buscarPartidos(criterios);
        view.mostrarResultados(resultados);
        return resultados;
    }
}

