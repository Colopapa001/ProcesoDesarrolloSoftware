package com.sportsmatching.presentacion.mvc.autenticacion.modelos;

import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.presentacion.mvc.autenticacion.servicios.AutenticacionService;

public class AuthModel {
    private Usuario usuarioActual;
    private final AutenticacionService autenticacionService;

    public AuthModel(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    public Usuario autenticar(String username, String password) {
        if (!autenticacionService.existeUsuario(username)) {
            throw new IllegalArgumentException("El usuario no existe. Por favor, regístrese primero.");
        }
        
        if (autenticacionService.verificarCredenciales(username, password)) {
            usuarioActual = autenticacionService.obtenerUsuario(username);
            return usuarioActual;
        }
        
        throw new IllegalArgumentException("Contraseña incorrecta");
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Usuario obtenerUsuario(String username) {
        return autenticacionService.obtenerUsuario(username);
    }
}

