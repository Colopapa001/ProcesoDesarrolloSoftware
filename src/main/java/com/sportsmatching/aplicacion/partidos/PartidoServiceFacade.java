package com.sportsmatching.aplicacion.partidos;

import com.sportsmatching.dominio.Location;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoModel;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoNotificacionService;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRepository;
import com.sportsmatching.presentacion.mvc.partido.modelos.PartidoService;
import com.sportsmatching.presentacion.mvc.busqueda.modelos.BusquedaModel;
import com.sportsmatching.aplicacion.emparejamiento.MatchmakingService;

import java.time.LocalDateTime;
import java.util.List;

public class PartidoServiceFacade {
    private final PartidoRepository partidoRepository;
    private final PartidoModel partidoModel;
    private final PartidoService partidoService;
    private final MatchmakingService matchmakingService;
    private final PartidoNotificacionService notificacionService;
    private final BusquedaModel busquedaModel;

    public PartidoServiceFacade(PartidoRepository partidoRepository,
                               PartidoModel partidoModel,
                               PartidoService partidoService,
                               MatchmakingService matchmakingService,
                               PartidoNotificacionService notificacionService,
                               BusquedaModel busquedaModel) {
        this.partidoRepository = partidoRepository;
        this.partidoModel = partidoModel;
        this.partidoService = partidoService;
        this.matchmakingService = matchmakingService;
        this.notificacionService = notificacionService;
        this.busquedaModel = busquedaModel;
    }

    public Partido crearPartidoCompleto(Deporte deporte, Usuario organizador, int jugadoresRequeridos,
                                       Location ubicacion, LocalDateTime fechaHora, int duracion,
                                       Nivel nivelMin, Nivel nivelMax) {
        // Usar PartidoModel para mantener consistencia con el patrón MVC
        return partidoModel.crearPartido(deporte, organizador, jugadoresRequeridos, 
                                       ubicacion, fechaHora, duracion, nivelMin, nivelMax);
    }

    public boolean inscribirJugadorYNotificar(Partido partido, Usuario usuario) {
        boolean resultado = partidoService.inscribirJugador(partido, usuario);
        if (resultado) {
            notificacionService.notificarCambioEstado(partido, partido.getEstado().getNombreEstado());
        }
        return resultado;
    }

    public List<Usuario> buscarYSugerirJugadores(Partido partido) {
        return matchmakingService.sugerir(partido);
    }

    public boolean finalizarPartidoCompleto(Partido partido) {
        // Lógica para finalizar partido
        notificacionService.notificarCambioEstado(partido, "FINALIZADO");
        return true;
    }
}

