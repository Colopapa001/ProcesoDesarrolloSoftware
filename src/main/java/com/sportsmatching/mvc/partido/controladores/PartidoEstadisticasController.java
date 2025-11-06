package com.sportsmatching.mvc.partido.controladores;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.mvc.partido.vistas.PartidoFormView;

public class PartidoEstadisticasController {
    private final PartidoModel model;
    private final PartidoFormView view;

    public PartidoEstadisticasController(PartidoModel model, PartidoFormView view) {
        this.model = model;
        this.view = view;
    }

    public boolean registrarEstadisticas(Partido partido, String estadisticas) {
        partido.getPartidoEstadisticas().registrarEstadisticas(estadisticas);
        view.mostrarEstadisticas(estadisticas);
        return true;
    }

    public boolean agregarComentario(Partido partido, String comentario) {
        partido.getPartidoEstadisticas().agregarComentario(comentario);
        view.mostrarComentarios(partido.getPartidoEstadisticas().obtenerComentarios());
        return true;
    }
}

