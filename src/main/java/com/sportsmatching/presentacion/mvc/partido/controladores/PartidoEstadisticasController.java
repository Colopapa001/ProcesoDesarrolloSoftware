package com.sportsmatching.presentacion.mvc.partido.controladores;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoFormView;

public class PartidoEstadisticasController {
    private final PartidoModel model;
    private final PartidoFormView view;

    public PartidoEstadisticasController(PartidoModel model, PartidoFormView view) {
        this.model = model;
        this.view = view;
    }

    public boolean registrarEstadisticas(Partido partido, String estadisticas) {
        partido.getPartidoEstadisticas().registrarEstadisticas(estadisticas);
        // Actualizar el partido en el repositorio
        model.actualizarPartido(partido);
        view.mostrarEstadisticas(estadisticas);
        return true;
    }

    public boolean agregarComentario(Partido partido, String comentario) {
        partido.getPartidoEstadisticas().agregarComentario(comentario);
        // Actualizar el partido en el repositorio
        model.actualizarPartido(partido);
        view.mostrarComentarios(partido.getPartidoEstadisticas().obtenerComentarios());
        return true;
    }
}

