package com.sportsmatching.aplicacion.strategy;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorNivel implements EmparejamientoStrategy {
    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos, Partido partido) {
        if (partido.getNivelMin() == null || partido.getNivelMax() == null) {
            return new ArrayList<>(candidatos);
        }
        
        return candidatos.stream()
            .filter(u -> u.getNivel() != null)
            .filter(u -> u.getNivel().getId() >= partido.getNivelMin().getId())
            .filter(u -> u.getNivel().getId() <= partido.getNivelMax().getId())
            .collect(Collectors.toList());
    }
}

