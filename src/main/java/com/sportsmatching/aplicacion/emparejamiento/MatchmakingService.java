package com.sportsmatching.aplicacion.emparejamiento;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class MatchmakingService {
    private EmparejamientoStrategy strategy;

    public MatchmakingService(EmparejamientoStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(EmparejamientoStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Usuario> sugerir(Partido partido) {
        // Obtener candidatos - esto debería venir de un repositorio
        // Por ahora retornamos lista vacía
        List<Usuario> candidatos = List.of();
        return strategy.emparejar(candidatos, partido);
    }

    public List<Usuario> filtrarPorNivel(List<Usuario> candidatos, Partido partido) {
        if (partido.getNivelMin() == null || partido.getNivelMax() == null) {
            return candidatos;
        }
        
        return candidatos.stream()
            .filter(u -> u.getNivel() != null)
            .filter(u -> u.getNivel().getId() >= partido.getNivelMin().getId())
            .filter(u -> u.getNivel().getId() <= partido.getNivelMax().getId())
            .collect(Collectors.toList());
    }
}

