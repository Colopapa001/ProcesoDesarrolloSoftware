package com.sportsmatching.presentacion.mvc.autenticacion.servicios;

import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.infraestructura.persistence.UsuarioRepository;

import java.util.UUID;

public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean verificarCredenciales(String username, String password) {
        return usuarioRepository.findByUsername(username)
            .map(u -> u.validarPassword(password))
            .orElse(false);
    }
    
    public boolean existeUsuario(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    public String generarToken(Usuario usuario) {
        return UUID.randomUUID().toString();
    }

    public Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
}

