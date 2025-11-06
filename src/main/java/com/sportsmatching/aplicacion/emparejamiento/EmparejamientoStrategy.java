package com.sportsmatching.aplicacion.emparejamiento;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

import java.util.List;

public interface EmparejamientoStrategy {
    List<Usuario> emparejar(List<Usuario> candidatos, Partido partido);
}

