package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.Partido;

import java.util.*;

/**
 * Adaptador que permite usar el repositorio de MVC desde infraestructura.persistence
 * Esto asegura que ambos repositorios compartan los mismos datos
 */
public class InMemoryPartidoRepository implements PartidoRepository {
    private final com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRepository repositorioMVC;

    public InMemoryPartidoRepository(com.sportsmatching.presentacion.mvc.partido.modelos.PartidoRepository repositorioMVC) {
        this.repositorioMVC = repositorioMVC;
    }

    @Override
    public Partido save(Partido partido) {
        return repositorioMVC.guardar(partido);
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return repositorioMVC.buscarPorId(id);
    }

    @Override
    public Collection<Partido> findAll() {
        return Collections.unmodifiableCollection(repositorioMVC.buscarTodos());
    }

    @Override
    public List<Partido> buscarTodos() {
        return repositorioMVC.buscarTodos();
    }
}

