package com.sportsmatching.repository;

import com.sportsmatching.dominio.Usuario;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<Long, Usuario> usuariosById = new HashMap<>();
    private final Map<String, Usuario> usuariosByUsername = new HashMap<>();
    private final Map<String, Usuario> usuariosByEmail = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idGenerator.getAndIncrement());
        }
        usuariosById.put(usuario.getId(), usuario);
        usuariosByUsername.put(usuario.getUsername(), usuario);
        usuariosByEmail.put(usuario.getEmail(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(usuariosById.get(id));
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usuariosByUsername.get(username));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return Optional.ofNullable(usuariosByEmail.get(email));
    }

    @Override
    public Collection<Usuario> findAll() {
        return Collections.unmodifiableCollection(usuariosById.values());
    }
}

