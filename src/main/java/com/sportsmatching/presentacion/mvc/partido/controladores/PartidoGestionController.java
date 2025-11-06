package com.sportsmatching.presentacion.mvc.partido.controladores;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.presentacion.mvc.partido.vistas.PartidoDetailView;
import com.sportsmatching.aplicacion.notificaciones.NotificacionSubject;

public class PartidoGestionController {
    private final PartidoModel model;
    private final PartidoDetailView view;
    private final NotificacionSubject notificacionSubject;

    public PartidoGestionController(PartidoModel model, PartidoDetailView view, 
                                   NotificacionSubject notificacionSubject) {
        this.model = model;
        this.view = view;
        this.notificacionSubject = notificacionSubject;
    }

    public boolean inscribirJugador(Partido partido, Usuario usuario) {
        boolean resultado = model.inscribirJugador(partido, usuario);
        if (resultado) {
            view.actualizarCupos(partido.getPartidoJugadores().obtenerCantidadDisponible(),
                                partido.getJugadoresRequeridos());
        }
        return resultado;
    }

    public boolean cancelarPartido(Partido partido) {
        partido.cancelar();
        view.actualizarEstado(partido.getEstado().getNombreEstado());
        return true;
    }

    public boolean confirmarPartido(Partido partido) {
        partido.confirmar();
        view.actualizarEstado(partido.getEstado().getNombreEstado());
        return true;
    }
}

