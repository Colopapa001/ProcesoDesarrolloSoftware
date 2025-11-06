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
        if (autenticacionService.verificarCredenciales(username, password)) {
            usuarioActual = autenticacionService.obtenerUsuario(username);
            return usuarioActual;
        }
        return null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}

