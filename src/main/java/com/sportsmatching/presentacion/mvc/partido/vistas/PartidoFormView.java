package com.sportsmatching.presentacion.mvc.partido.vistas;

import java.util.List;

public class PartidoFormView {
    public void mostrarFormularioCreacion() {
        System.out.println("=== Formulario de Creación de Partido ===");
    }

    public void mostrarEstadisticas(String estadisticas) {
        System.out.println("Estadísticas: " + estadisticas);
    }

    public void mostrarComentarios(List<String> comentarios) {
        System.out.println("=== Comentarios ===");
        comentarios.forEach(c -> System.out.println("- " + c));
    }
}

