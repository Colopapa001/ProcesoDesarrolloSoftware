package com.sportsmatching.aplicacion.servicios;

import com.sportsmatching.dominio.Location;

/**
 * Permite calcular la distancia entre dos ubicaciones.
 * Facilita el intercambio de implementaciones (por ejemplo, mocks en tests).
 */
public interface DistanceCalculator {
    double calcularDistancia(Location loc1, Location loc2);
}

