package com.sportsmatching.strategy;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorHistorial implements EmparejamientoStrategy {
    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos, Partido partido) {
        // Implementación simplificada - en producción se consultaría el historial
        return new ArrayList<>(candidatos);
    }
}

