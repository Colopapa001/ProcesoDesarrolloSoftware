package com.sportsmatching.dominio;

public class Location {
    private double latitud;
    private double longitud;
    private String descripcion; // Opcional: descripción de la ubicación

    public Location(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Location(double latitud, double longitud, String descripcion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public String getDescripcion() { return descripcion != null ? descripcion : String.format("(%.6f, %.6f)", latitud, longitud); }

    @Override
    public String toString() {
        return descripcion != null ? descripcion : String.format("(%.6f, %.6f)", latitud, longitud);
    }
}

