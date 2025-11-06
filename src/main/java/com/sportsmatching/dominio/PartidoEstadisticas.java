package com.sportsmatching.dominio;

import java.util.ArrayList;
import java.util.List;

public class PartidoEstadisticas {
    private String estadisticas;
    private List<String> comentarios;

    public PartidoEstadisticas() {
        this.estadisticas = "";
        this.comentarios = new ArrayList<>();
    }

    public void registrarEstadisticas(String estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void agregarComentario(String comentario) {
        this.comentarios.add(comentario);
    }

    public String obtenerEstadisticas() {
        return estadisticas;
    }

    public List<String> obtenerComentarios() {
        return new ArrayList<>(comentarios);
    }
}

