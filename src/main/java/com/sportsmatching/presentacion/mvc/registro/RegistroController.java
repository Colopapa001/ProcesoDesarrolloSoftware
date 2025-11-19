package com.sportsmatching.presentacion.mvc.registro;

import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.presentacion.mvc.registro.modelos.RegistroModel;

public class RegistroController {
    private final RegistroModel model;
    private final RegistroView view;

    public RegistroController(RegistroModel model, RegistroView view) {
        this.model = model;
        this.view = view;
    }

    public Boolean registrarUsuario(String username, String email, String password, 
                                   com.sportsmatching.dominio.catalogos.Nivel nivel,
                                   com.sportsmatching.dominio.catalogos.Deporte deporteFavorito,
                                   com.sportsmatching.dominio.Location ubicacion) {
        try {
            Usuario usuario = model.crearUsuario(username, email, password, nivel, deporteFavorito, ubicacion);
            if (usuario != null) {
                view.mostrarExito();
                return true;
            } else {
                view.mostrarError("Error al crear el usuario");
                return false;
            }
        } catch (IllegalArgumentException e) {
            // Mostrar mensaje de error específico
            view.mostrarError(e.getMessage());
            return false;
        } catch (RuntimeException e) {
            view.mostrarError("Error inesperado: " + e.getMessage());
            return false;
        }
    }
}

