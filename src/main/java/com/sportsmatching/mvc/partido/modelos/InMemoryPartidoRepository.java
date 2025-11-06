package com.sportsmatching.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPartidoRepository implements PartidoRepository {
    private final Map<Long, Partido> partidosById = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Partido guardar(Partido partido) {
        if (partido.getId() == null) {
            partido.setId(idGenerator.getAndIncrement());
        }
        partidosById.put(partido.getId(), partido);
        return partido;
    }

    @Override
    public Optional<Partido> buscarPorId(Long id) {
        return Optional.ofNullable(partidosById.get(id));
    }

    @Override
    public List<Partido> buscarTodos() {
        return new ArrayList<>(partidosById.values());
    }

    @Override
    public Partido actualizar(Partido partido) {
        partidosById.put(partido.getId(), partido);
        return partido;
    }
}

