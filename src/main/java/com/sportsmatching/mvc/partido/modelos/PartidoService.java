package com.sportsmatching.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.PartidoValidacion;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.strategy.MatchmakingService;

public class PartidoService {
    private final PartidoValidacion validacion;
    private final MatchmakingService matchmakingService;

    public PartidoService(PartidoValidacion validacion, MatchmakingService matchmakingService) {
        this.validacion = validacion;
        this.matchmakingService = matchmakingService;
    }

    public boolean inscribirJugador(Partido partido, Usuario usuario) {
        if (!puedeInscribirJugador(partido, usuario)) {
            return false;
        }
        boolean resultado = partido.getPartidoJugadores().agregarJugador(usuario);
        if (resultado) {
            partido.getEstado().onJugadorAgregado(partido);
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
}

