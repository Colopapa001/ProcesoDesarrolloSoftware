package com.sportsmatching.aplicacion.servicios;

import com.sportsmatching.dominio.Location;

public class DistanciaService implements DistanceCalculator {
    // Radio de la Tierra en kilómetros
    private static final double RADIO_TIERRA_KM = 6371.0;

    /**
     * Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine
     * @param loc1 Primera ubicación
     * @param loc2 Segunda ubicación
     * @return Distancia en kilómetros
     */
    @Override
    public double calcularDistancia(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitud());
        double lon1 = Math.toRadians(loc1.getLongitud());
        double lat2 = Math.toRadians(loc2.getLatitud());
        double lon2 = Math.toRadians(loc2.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }
}

