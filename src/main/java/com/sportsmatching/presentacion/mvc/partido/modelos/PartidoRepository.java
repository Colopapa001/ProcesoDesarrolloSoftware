package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;

import java.util.List;
import java.util.Optional;

public interface PartidoRepository {
    Partido guardar(Partido partido);
    Optional<Partido> buscarPorId(Long id);
    List<Partido> buscarTodos();
    Partido actualizar(Partido partido);
}

