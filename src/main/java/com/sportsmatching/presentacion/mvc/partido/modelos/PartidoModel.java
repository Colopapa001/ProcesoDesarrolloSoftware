package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

public class PartidoModel {
    private Partido partidoActual;
    private final PartidoRepository repository;
    private final PartidoService service;
    private final PartidoNotificacionService notificacionService;

    public PartidoModel(PartidoRepository repository, PartidoService service,
                       PartidoNotificacionService notificacionService) {
        this.repository = repository;
        this.service = service;
        this.notificacionService = notificacionService;
    }

    public Partido crearPartido(com.sportsmatching.dominio.catalogos.Deporte deporte,
                               Usuario organizador,
                               int jugadoresRequeridos,
                               com.sportsmatching.dominio.Location ubicacion,
                               java.time.LocalDateTime fechaHora,
                               int duracion,
                               com.sportsmatching.dominio.catalogos.Nivel nivelMin,
                               com.sportsmatching.dominio.catalogos.Nivel nivelMax) {
        Partido partido = new Partido(deporte, organizador, jugadoresRequeridos,
                                     ubicacion, fechaHora, duracion, nivelMin, nivelMax);
        partido = repository.guardar(partido);
        notificacionService.notificarCreacion(partido);
        this.partidoActual = partido;
        return partido;
    }

    public Partido obtenerPartido(Long id) {
        partidoActual = repository.buscarPorId(id).orElse(null);
        return partidoActual;
    }

    public boolean inscribirJugador(Partido partido, Usuario usuario) {
        boolean resultado = service.inscribirJugador(partido, usuario);
        if (resultado) {
            repository.actualizar(partido);
        }
        return resultado;
    }

    public boolean removerJugador(Partido partido, Usuario usuario) {
        boolean resultado = service.removerJugador(partido, usuario);
        if (resultado) {
            repository.actualizar(partido);
        }
        return resultado;
    }

    public Partido getPartidoActual() {
        return partidoActual;
    }

    public void actualizarPartido(Partido partido) {
        repository.actualizar(partido);
        this.partidoActual = partido;
    }
}

