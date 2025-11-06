package com.sportsmatching.presentacion.mvc.registro;

public class RegistroView {
    public void mostrarFormulario() {
        System.out.println("=== Formulario de Registro ===");
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }

    public void mostrarExito() {
        System.out.println("Usuario registrado exitosamente");
    }
}

