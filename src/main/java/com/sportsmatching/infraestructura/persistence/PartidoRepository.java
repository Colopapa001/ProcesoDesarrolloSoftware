package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.Partido;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PartidoRepository {
    Partido save(Partido partido);
    Optional<Partido> findById(Long id);
    Collection<Partido> findAll();
    List<Partido> buscarTodos();
}

