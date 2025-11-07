package com.sportsmatching.presentacion.mvc.busqueda;

import com.sportsmatching.dominio.Partido;

import java.util.List;

public class BusquedaView {
    public void mostrarResultados(List<Partido> partidos) {
        System.out.println("=== Resultados de Búsqueda ===");
        partidos.forEach(p -> System.out.println("Partido: " + p.getDeporte().getNombre()));
    }

    public void mostrarFormularioBusqueda() {
        System.out.println("=== Formulario de Búsqueda ===");
    }

    public void mostrarDetallePartido(Partido partido) {
        System.out.println("=== Detalle del Partido ===");
        System.out.println("ID: " + partido.getId());
        System.out.println("Deporte: " + partido.getDeporte().getNombre());
        System.out.println("Ubicación: " + partido.getUbicacion().getDescripcion());
        System.out.println("Coordenadas: (" + partido.getUbicacion().getLatitud() + ", " + partido.getUbicacion().getLongitud() + ")");
        System.out.println("Estado: " + partido.getEstado().getNombreEstado());
        System.out.println("Jugadores: " + partido.getPartidoJugadores().getJugadores().size() + "/" + partido.getJugadoresRequeridos());
        System.out.println("Fecha y hora: " + partido.getFechaHora());
        System.out.println("Duración: " + partido.getDuracion() + " minutos");

        var estadisticas = partido.getPartidoEstadisticas();
        if (estadisticas != null) {
            String resumen = estadisticas.obtenerEstadisticas();
            var comentarios = estadisticas.obtenerComentarios();

            if (resumen != null && !resumen.isBlank()) {
                System.out.println("Estadísticas: " + resumen);
            } else {
                System.out.println("Estadísticas: (aún no registradas)");
            }

            if (comentarios != null && !comentarios.isEmpty()) {
                System.out.println("Comentarios:");
                comentarios.forEach(c -> System.out.println("  - " + c));
            } else {
                System.out.println("Comentarios: (sin comentarios)");
            }
        } else {
            System.out.println("Estadísticas: (aún no registradas)");
            System.out.println("Comentarios: (sin comentarios)");
        }
    }
}

