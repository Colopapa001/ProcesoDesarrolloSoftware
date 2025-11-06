package com.sportsmatching.dominio;

public class Location {
    private double latitud;
    private double longitud;
    private String descripcion; // Opcional: descripción de la ubicación

    public Location(double latitud, double longitud) {
        validarCoordenadas(latitud, longitud);
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Location(double latitud, double longitud, String descripcion) {
        validarCoordenadas(latitud, longitud);
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }
    
    private void validarCoordenadas(double latitud, double longitud) {
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90 grados. Valor ingresado: " + latitud);
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180 grados. Valor ingresado: " + longitud);
        }
    }

    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public String getDescripcion() { return descripcion != null ? descripcion : String.format("(%.6f, %.6f)", latitud, longitud); }

    @Override
    public String toString() {
        return descripcion != null ? descripcion : String.format("(%.6f, %.6f)", latitud, longitud);
    }
}

