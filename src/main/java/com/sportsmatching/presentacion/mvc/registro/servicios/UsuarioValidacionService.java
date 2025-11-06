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
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email debe ser válido (debe contener @)");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return true;
    }
    
    public boolean validarUbicacion(com.sportsmatching.dominio.Location ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("La ubicación no puede ser nula");
        }
        // La validación de coordenadas se hace en el constructor de Location
        return true;
    }
}

