package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.Partido;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPartidoRepository implements PartidoRepository {
    private final Map<Long, Partido> partidosById = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Partido save(Partido partido) {
        if (partido.getId() == null) {
            partido.setId(idGenerator.getAndIncrement());
        }
        partidosById.put(partido.getId(), partido);
        return partido;
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return Optional.ofNullable(partidosById.get(id));
    }

    @Override
    public Collection<Partido> findAll() {
        return Collections.unmodifiableCollection(partidosById.values());
    }

    @Override
    public List<Partido> buscarTodos() {
        return new ArrayList<>(partidosById.values());
    }
}

