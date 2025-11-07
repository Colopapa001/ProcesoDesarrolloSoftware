package com.sportsmatching.presentacion.mvc.busqueda;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Location;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.infra.MockDomainDataStore;
import com.sportsmatching.presentacion.mvc.busqueda.modelos.BusquedaModel;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BusquedaController {
    private final BusquedaModel model;
    private final BusquedaView view;

    public BusquedaController(BusquedaModel model, BusquedaView view) {
        this.model = model;
        this.view = view;
    }

    public List<Partido> buscarPartidos(Map<String, Object> criterios) {
        List<Partido> resultados = model.buscarPartidos(criterios);
        // Si el modelo devolvió resultados, mostrarlos y devolverlos
        if (resultados != null && !resultados.isEmpty()) {
            view.mostrarResultados(resultados);
            return resultados;
        }

        // Fallback: intentar usar MockDomainDataStore cuando no haya resultados reales
        try {
            String ubicDesc = "";
            boolean disponibilidad = false;
            String nivelNombre = null;
            String deporteNombre = null;

            Object locObj = criterios != null ? criterios.get("ubicacionUsuario") : null;
            if (locObj instanceof Location) {
                ubicDesc = ((Location) locObj).getDescripcion();
            } else if (locObj instanceof String) {
                ubicDesc = (String) locObj;
            }

            Object dispObj = criterios != null ? criterios.get("disponibilidad") : null;
            if (dispObj instanceof Boolean) {
                disponibilidad = (Boolean) dispObj;
            }

            Object nivelObj = criterios != null ? criterios.get("nivelUsuario") : null;
            if (nivelObj instanceof Usuario) {
                Nivel n = ((Usuario) nivelObj).getNivel();
                nivelNombre = n != null ? n.getNombre() : null;
            } else if (nivelObj instanceof Nivel) {
                nivelNombre = ((Nivel) nivelObj).getNombre();
            } else if (nivelObj instanceof String) {
                nivelNombre = (String) nivelObj;
            }

            Object deporteObj = criterios != null ? criterios.get("deporteFavorito") : null;
            if (deporteObj instanceof Usuario) {
                if (((Usuario) deporteObj).getDeporteFavorito() != null) {
                    deporteNombre = ((Usuario) deporteObj).getDeporteFavorito().getNombre();
                }
            } else if (deporteObj instanceof String) {
                deporteNombre = (String) deporteObj;
            }

            var mockResultados = MockDomainDataStore.searchPartidos(ubicDesc, disponibilidad, nivelNombre, deporteNombre);
            if (mockResultados != null && !mockResultados.isEmpty()) {
                System.out.println("\n=== Resultados (desde datos mock) ===");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (var p : mockResultados) {
                    int inscritos = (p.partidoJugadores == null || p.partidoJugadores.jugadores == null)
                            ? 0 : p.partidoJugadores.jugadores.size();
                    System.out.println("ID: " + p.id +
                            " | Deporte: " + p.deporte +
                            " | Ubicación: " + (p.ubicacion != null ? p.ubicacion.descripcion : "n/d") +
                            " | Fecha: " + (p.fechaHora != null ? p.fechaHora.format(fmt) : "n/d") +
                            " | Cupos: " + inscritos + "/" + p.jugadoresRequeridos +
                            " | Nivel: " + p.nivelMin + "-" + p.nivelMax +
                            " | Organizador: " + p.organizadorUsername);
                }
                // Retornamos lista vacía porque no tenemos objetos Partido mapeados desde el DTO mock
                return Collections.emptyList();
            }
        } catch (NoClassDefFoundError | Exception ignored) {
            // Si no existe MockDomainDataStore o falla, continuar
        }

        // No se encontraron partidos
        view.mostrarResultados(Collections.emptyList());
        return Collections.emptyList();
    }
}