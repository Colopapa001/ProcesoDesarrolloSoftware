package com.sportsmatching.presentacion.mvc.registro.servicios;

import com.sportsmatching.infraestructura.persistence.UsuarioRepository;

public class UsuarioValidacionService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioValidacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean validarUnicidad(String email, String username) {
        return usuarioRepository.findByEmail(email).isEmpty() &&
               usuarioRepository.findByUsername(username).isEmpty();
    }

    public boolean validarDatos(String username, String email, String password) {
        return username != null && !username.trim().isEmpty() &&
               email != null && email.contains("@") &&
               password != null && password.length() >= 6;
    }
}

