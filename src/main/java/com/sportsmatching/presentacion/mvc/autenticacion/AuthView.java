package com.sportsmatching.presentacion.mvc.autenticacion;

public class AuthView {
    public void mostrarLogin() {
        System.out.println("=== Iniciar Sesión ===");
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error de autenticación: " + mensaje);
    }
}

