package com.sportsmatching.aplicacion.strategy;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorCercania implements EmparejamientoStrategy {
    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos, Partido partido) {
        // Implementación simplificada - en producción se calcularía la distancia real
        return new ArrayList<>(candidatos);
    }
}

