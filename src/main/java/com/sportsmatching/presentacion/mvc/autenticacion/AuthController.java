package com.sportsmatching.presentacion.mvc.autenticacion;

import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.presentacion.mvc.autenticacion.modelos.AuthModel;

public class AuthController {
    private final AuthModel model;
    private final AuthView view;

    public AuthController(AuthModel model, AuthView view) {
        this.model = model;
        this.view = view;
    }

    public Usuario login(String username, String password) {
        try {
            Usuario usuario = model.autenticar(username, password);
            if (usuario != null) {
                return usuario;
            } else {
                view.mostrarError("Credenciales inválidas");
                return null;
            }
        } catch (Exception e) {
            view.mostrarError(e.getMessage());
            return null;
        }
    }

    public void logout() {
        model.setUsuarioActual(null);
    }

    public Usuario obtenerUsuarioActual() {
        return model.getUsuarioActual();
    }

    public Usuario obtenerUsuario(String username) {
        return model.obtenerUsuario(username);
    }
}

