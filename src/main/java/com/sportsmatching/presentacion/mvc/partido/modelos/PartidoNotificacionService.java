package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.aplicacion.observer.NotificacionSubject;

public class PartidoNotificacionService {
    private final NotificacionSubject subject;

    public PartidoNotificacionService(NotificacionSubject subject) {
        this.subject = subject;
    }

    public void notificarCambioEstado(Partido partido, String estado) {
        subject.notify("CAMBIO_ESTADO", partido);
    }

    public void notificarCreacion(Partido partido) {
        subject.notify("CREACION", partido);
    }
}

