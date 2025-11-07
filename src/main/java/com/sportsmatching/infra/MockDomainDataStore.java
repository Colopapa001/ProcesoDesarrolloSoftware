package com.sportsmatching.infra;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Almacenamiento en memoria para datos de dominio (usuarios, partidos, locations, estadisticas).
 * Persiste mientras la JVM esté viva.
 */
public class MockDomainDataStore {
    public static class LocationDTO {
        public double latitud;
        public double longitud;
        public String descripcion;
        public LocationDTO(double lat, double lon, String desc) { this.latitud = lat; this.longitud = lon; this.descripcion = desc;}
    }

    public static class UsuarioDTO {
        public Long id;
        public String username;
        public String email;
        public String password;
        public String nivel;         // e.g. "Intermedio"
        public String deporteFavorito;// e.g. "Fútbol"
        public LocationDTO ubicacion;
        public UsuarioDTO(Long id, String username, String email, String password, String nivel, String deporte, LocationDTO ubicacion) {
            this.id = id; this.username = username; this.email = email; this.password = password;
            this.nivel = nivel; this.deporteFavorito = deporte; this.ubicacion = ubicacion;
        }
    }

    public static class PartidoEstadisticasDTO {
        public String estadisticas;
        public List<String> comentarios = new ArrayList<>();
        public PartidoEstadisticasDTO(String estadisticas) { this.estadisticas = estadisticas; }
    }

    public static class PartidoJugadoresDTO {
        public List<UsuarioDTO> jugadores = new ArrayList<>();
        public int jugadoresRequeridos;
        public PartidoJugadoresDTO(int jugadoresRequeridos) { this.jugadoresRequeridos = jugadoresRequeridos; }
    }

    public static class PartidoDTO {
        public int id;
        public String deporte;
        public String organizadorUsername;
        public int jugadoresRequeridos;
        public String estado; // por simplicidad: "CREADO", "CERRADO", etc.
        public LocationDTO ubicacion;
        public LocalDateTime fechaHora;
        public int duracion;
        public String nivelMin;
        public String nivelMax;
        public Date fechaCreacion;
        public PartidoJugadoresDTO partidoJugadores;
        public PartidoEstadisticasDTO partidoEstadisticas;

        public PartidoDTO(int id, String deporte, String organizadorUsername, int jugadoresRequeridos,
                          String estado, LocationDTO ubicacion, LocalDateTime fechaHora, int duracion,
                          String nivelMin, String nivelMax, Date fechaCreacion) {
            this.id = id; this.deporte = deporte; this.organizadorUsername = organizadorUsername;
            this.jugadoresRequeridos = jugadoresRequeridos; this.estado = estado; this.ubicacion = ubicacion;
            this.fechaHora = fechaHora; this.duracion = duracion; this.nivelMin = nivelMin; this.nivelMax = nivelMax;
            this.fechaCreacion = fechaCreacion;
        }

        public boolean isAvailable() {
            if (partidoJugadores == null) return true;
            return partidoJugadores.jugadores.size() < partidoJugadores.jugadoresRequeridos;
        }
    }

    // Almacenamiento
    private static final Map<Long, UsuarioDTO> usuarios = new LinkedHashMap<>();
    private static final Map<Integer, PartidoDTO> partidos = new LinkedHashMap<>();
    private static final AtomicInteger usuarioSeq = new AtomicInteger(1);
    private static final AtomicInteger partidoSeq = new AtomicInteger(1);

    // CRUD Usuario
    public static synchronized UsuarioDTO addUsuario(String username, String email, String password, String nivel, String deporte, LocationDTO ubicacion) {
        long id = usuarioSeq.getAndIncrement();
        UsuarioDTO u = new UsuarioDTO(id, username, email, password, nivel, deporte, ubicacion);
        usuarios.put(id, u);
        return u;
    }

    public static synchronized Optional<UsuarioDTO> findUsuarioByUsername(String username) {
        return usuarios.values().stream().filter(u -> u.username.equalsIgnoreCase(username)).findFirst();
    }

    public static synchronized Optional<UsuarioDTO> authenticate(String username, String password) {
        return usuarios.values().stream().filter(u -> u.username.equalsIgnoreCase(username) && u.password.equals(password)).findFirst();
    }

    public static synchronized Collection<UsuarioDTO> allUsuarios() { return usuarios.values(); }

    // CRUD Partido
    public static synchronized PartidoDTO addPartido(String deporte, String organizadorUsername, int jugadoresRequeridos,
                                                    String estado, LocationDTO ubicacion, LocalDateTime fechaHora,
                                                    int duracion, String nivelMin, String nivelMax) {
        int id = partidoSeq.getAndIncrement();
        PartidoDTO p = new PartidoDTO(id, deporte, organizadorUsername, jugadoresRequeridos, estado, ubicacion, fechaHora, duracion, nivelMin, nivelMax, new Date());
        p.partidoJugadores = new PartidoJugadoresDTO(jugadoresRequeridos);
        p.partidoEstadisticas = new PartidoEstadisticasDTO("");
        partidos.put(id, p);
        return p;
    }

    public static synchronized Optional<PartidoDTO> findPartidoById(int id) { return Optional.ofNullable(partidos.get(id)); }
    public static synchronized Collection<PartidoDTO> allPartidos() { return partidos.values(); }

    public static synchronized void addJugadorToPartido(int partidoId, UsuarioDTO usuario) {
        PartidoDTO p = partidos.get(partidoId);
        if (p != null) {
            if (p.partidoJugadores == null) p.partidoJugadores = new PartidoJugadoresDTO(p.jugadoresRequeridos);
            if (p.partidoJugadores.jugadores.size() < p.partidoJugadores.jugadoresRequeridos) {
                p.partidoJugadores.jugadores.add(usuario);
            }
        }
    }

    public static synchronized void setEstadisticas(int partidoId, String estadisticas, List<String> comentarios) {
        PartidoDTO p = partidos.get(partidoId);
        if (p != null) {
            if (p.partidoEstadisticas == null) p.partidoEstadisticas = new PartidoEstadisticasDTO(estadisticas);
            p.partidoEstadisticas.estadisticas = estadisticas;
            p.partidoEstadisticas.comentarios = new ArrayList<>(comentarios);
        }
    }

    // Búsqueda simple
    public static synchronized List<PartidoDTO> searchPartidos(String ubicacionDescripcion, boolean filtrarDisponibilidad, String nivelUsuario, String deporte) {
        Map<String,Integer> order = Map.of("Principiante",1,"Intermedio",2,"Avanzado",3);
        return partidos.values().stream()
                .filter(p -> ubicacionDescripcion == null || ubicacionDescripcion.isEmpty() || (p.ubicacion != null && p.ubicacion.descripcion.toLowerCase().contains(ubicacionDescripcion.toLowerCase())))
                .filter(p -> deporte == null || deporte.isEmpty() || p.deporte.equalsIgnoreCase(deporte))
                .filter(p -> {
                    if (!filtrarDisponibilidad) return true;
                    return p.isAvailable();
                })
                .filter(p -> {
                    if (nivelUsuario == null || nivelUsuario.isEmpty()) return true;
                    int user = order.getOrDefault(nivelUsuario, 2);
                    int min = order.getOrDefault(p.nivelMin, 1);
                    int max = order.getOrDefault(p.nivelMax, 3);
                    return user >= min && user <= max;
                })
                .sorted(Comparator.comparing(pt -> pt.fechaHora))
                .collect(Collectors.toList());
    }
}