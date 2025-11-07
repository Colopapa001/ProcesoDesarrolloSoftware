package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.Usuario;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<Long, Usuario> usuariosById = new HashMap<>();
    // Usar claves normalizadas a minúsculas para evitar duplicados por diferencias de mayúsculas
    private final Map<String, Usuario> usuariosByUsername = new HashMap<>();
    private final Map<String, Usuario> usuariosByEmail = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idGenerator.getAndIncrement());
        }
        usuariosById.put(usuario.getId(), usuario);
        // Guardar usando claves normalizadas a minúsculas para evitar duplicados
        usuariosByUsername.put(usuario.getUsername().toLowerCase(), usuario);
        usuariosByEmail.put(usuario.getEmail().toLowerCase(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(usuariosById.get(id));
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        // Búsqueda case-insensitive usando clave normalizada
        return Optional.ofNullable(usuariosByUsername.get(username.toLowerCase()));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        // Búsqueda case-insensitive usando clave normalizada
        return Optional.ofNullable(usuariosByEmail.get(email.toLowerCase()));
    }

    @Override
    public Collection<Usuario> findAll() {
        return Collections.unmodifiableCollection(usuariosById.values());
    }
}

