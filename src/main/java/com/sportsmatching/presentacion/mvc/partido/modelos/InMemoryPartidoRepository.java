package com.sportsmatching.presentacion.mvc.partido.modelos;

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
        } else {
            // Sincronizar el generador de IDs para evitar conflictos
            long currentId = partido.getId();
            long currentGenerator = idGenerator.get();
            if (currentId >= currentGenerator) {
                idGenerator.set(currentId + 1);
            }
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

