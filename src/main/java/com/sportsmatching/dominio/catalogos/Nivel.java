package com.sportsmatching.dominio.catalogos;

public class Nivel {
    private Long id;
    private String nombre;

    public Nivel(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}

