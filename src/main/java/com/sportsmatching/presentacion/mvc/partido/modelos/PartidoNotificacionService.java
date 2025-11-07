package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;

public interface PartidoNotificacionService {
    void notificarCambioEstado(Partido partido, String estado);

    void notificarCreacion(Partido partido);
}

