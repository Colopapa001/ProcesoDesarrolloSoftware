package com.sportsmatching.infraestructura.persistence;

import com.sportsmatching.dominio.Usuario;

import java.util.Collection;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Collection<Usuario> findAll();
}

