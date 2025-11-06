package com.sportsmatching.presentacion.mvc.partido.controladores;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoListView;

import java.util.List;

public class PartidoCreacionController {
    private final PartidoModel model;
    private final PartidoListView view;

    public PartidoCreacionController(PartidoModel model, PartidoListView view) {
        this.model = model;
        this.view = view;
    }

    public Partido crearPartido(com.sportsmatching.dominio.catalogos.Deporte deporte,
                               com.sportsmatching.dominio.Usuario organizador,
                               int jugadoresRequeridos,
                               com.sportsmatching.dominio.Location ubicacion,
                               java.time.LocalDateTime fechaHora,
                               int duracion,
                               com.sportsmatching.dominio.catalogos.Nivel nivelMin,
                               com.sportsmatching.dominio.catalogos.Nivel nivelMax) {
        Partido partido = model.crearPartido(deporte, organizador, jugadoresRequeridos, 
                                            ubicacion, fechaHora, duracion, nivelMin, nivelMax);
        view.mostrarLista(List.of(partido));
        return partido;
    }
}

