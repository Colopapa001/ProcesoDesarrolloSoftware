package com.sportsmatching.presentacion.mvc.busqueda.servicios;

import com.sportsmatching.dominio.Location;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.aplicacion.servicios.DistanceCalculator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BusquedaFiltroService {
    private final DistanceCalculator distanceCalculator;
    private static final double DISTANCIA_MAXIMA_KM = 50.0; // Radio de búsqueda en km

    public BusquedaFiltroService(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public List<Partido> filtrarPorDisponibilidad(List<Partido> partidos) {
        return partidos.stream()
            .filter(p -> p.getPartidoJugadores().obtenerCantidadDisponible() > 0)
            .collect(Collectors.toList());
    }

    public List<Partido> aplicarFiltros(List<Partido> partidos, Map<String, Object> filtros) {
        List<Partido> resultado = partidos;
        
        // Filtrar por disponibilidad
        if (filtros.containsKey("disponibilidad") && (Boolean) filtros.get("disponibilidad")) {
            resultado = filtrarPorDisponibilidad(resultado);
        }
        
        // Filtrar por ubicación del usuario (cercanía)
        if (filtros.containsKey("ubicacionUsuario")) {
            Location ubicacionUsuario = (Location) filtros.get("ubicacionUsuario");
            resultado = resultado.stream()
                .filter(p -> {
                    double distancia = distanceCalculator.calcularDistancia(ubicacionUsuario, p.getUbicacion());
                    return distancia <= DISTANCIA_MAXIMA_KM;
                })
                .sorted(Comparator.comparingDouble(p -> 
                    distanceCalculator.calcularDistancia(ubicacionUsuario, p.getUbicacion())))
                .collect(Collectors.toList());
        }
        
        // Filtrar por nivel del usuario
        if (filtros.containsKey("nivelUsuario")) {
            Usuario usuario = (Usuario) filtros.get("nivelUsuario");
            if (usuario.getNivel() != null) {
                resultado = resultado.stream()
                    .filter(p -> {
                        if (p.getNivelMin() == null || p.getNivelMax() == null) {
                            return true; // Si no tiene restricción de nivel, aceptar
                        }
                        Long nivelUsuarioId = usuario.getNivel().getId();
                        return nivelUsuarioId >= p.getNivelMin().getId() && 
                               nivelUsuarioId <= p.getNivelMax().getId();
                    })
                    .collect(Collectors.toList());
            }
        }
        
        // Filtrar por deporte favorito del usuario
        if (filtros.containsKey("deporteFavorito")) {
            Usuario usuario = (Usuario) filtros.get("deporteFavorito");
            if (usuario.getDeporteFavorito() != null) {
                resultado = resultado.stream()
                    .filter(p -> p.getDeporte().getId().equals(usuario.getDeporteFavorito().getId()))
                    .collect(Collectors.toList());
            }
        }
        
        return resultado;
    }
}

