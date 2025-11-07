package com.sportsmatching.dominio;

import java.time.LocalDateTime;

public class PartidoValidacion {
    public boolean verificarHorarioComienzo(Partido partido) {
        return partido.getFechaHora().isAfter(LocalDateTime.now());
    }

    public boolean verificarHorarioFinalizacion(Partido partido) {
        LocalDateTime fin = partido.getFechaHora().plusMinutes(partido.getDuracion());
        return fin.isBefore(LocalDateTime.now());
    }

    public boolean validarNivelJugador(Partido partido, Usuario usuario) {
        if (partido.getNivelMin() == null || partido.getNivelMax() == null) {
            return true;
        }
        if (usuario.getNivel() == null) {
            return false;
        }
        // Asumiendo que los niveles tienen un orden
        return usuario.getNivel().getId() >= partido.getNivelMin().getId() 
            && usuario.getNivel().getId() <= partido.getNivelMax().getId();
    }

    public boolean validarCapacidad(Partido partido) {
        return partido.getPartidoJugadores().obtenerCantidadDisponible() > 0;
    }
}

