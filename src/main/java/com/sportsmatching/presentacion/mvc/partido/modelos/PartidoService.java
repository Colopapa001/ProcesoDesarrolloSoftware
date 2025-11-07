package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.PartidoValidacion;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.aplicacion.emparejamiento.MatchmakingService;
import com.sportsmatching.aplicacion.partidos.PartidoState;

public class PartidoService {
    private final PartidoValidacion validacion;
    private final MatchmakingService matchmakingService;
    private final PartidoNotificacionService notificacionService;

    public PartidoService(PartidoValidacion validacion,
                          MatchmakingService matchmakingService,
                          PartidoNotificacionService notificacionService) {
        this.validacion = validacion;
        this.matchmakingService = matchmakingService;
        this.notificacionService = notificacionService;
    }

    public boolean inscribirJugador(Partido partido, Usuario usuario) {
        if (!puedeInscribirJugador(partido, usuario)) {
            return false;
        }
        PartidoState estadoAnterior = partido.getEstado();
        boolean resultado = partido.getPartidoJugadores().agregarJugador(usuario);
        if (resultado) {
            partido.getEstado().onJugadorAgregado(partido);
            // Notificar si el estado cambió
            if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                notificacionService.notificarCambioEstado(partido, partido.getEstado().getNombreEstado());
            }
        }
        return resultado;
    }

    public boolean verificarCompletitud(Partido partido) {
        return partido.getPartidoJugadores().verificarCompletitud();
    }

    public boolean puedeInscribirJugador(Partido partido, Usuario usuario) {
        return validacion.validarNivelJugador(partido, usuario) &&
               partido.getEstado().puedeAgregarJugador(partido) &&
               partido.getPartidoJugadores().puedeAgregarJugador(usuario);
    }

    public boolean removerJugador(Partido partido, Usuario usuario) {
        if (partido.getPartidoJugadores().getJugadores().contains(usuario)) {
            PartidoState estadoAnterior = partido.getEstado();
            boolean resultado = partido.getPartidoJugadores().removerJugador(usuario);
            if (resultado) {
                // Cuando se remueve un jugador, el estado podría cambiar
                partido.getEstado().onJugadorRemovido(partido);
                // Notificar si el estado cambió
                if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                    notificacionService.notificarCambioEstado(partido, partido.getEstado().getNombreEstado());
                }
            }
            return resultado;
        }
        return false;
    }
}

