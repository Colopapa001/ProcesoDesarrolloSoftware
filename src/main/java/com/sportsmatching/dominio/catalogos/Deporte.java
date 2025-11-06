package com.sportsmatching.dominio.catalogos;

public class Deporte {
    private Long id;
    private String nombre;

    public Deporte(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}

