package com.sportsmatching.dominio;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;
import com.sportsmatching.aplicacion.state.PartidoState;
import com.sportsmatching.aplicacion.state.NecesitamosJugadores;
import com.sportsmatching.aplicacion.observer.NotificacionSubject;

import java.time.LocalDateTime;
import java.util.Date;

public class Partido extends NotificacionSubject {
    private Long id;
    private Deporte deporte;
    private Usuario organizador;
    private int jugadoresRequeridos;
    private PartidoState estado;
    private Location ubicacion;
    private LocalDateTime fechaHora;
    private int duracion;
    private Nivel nivelMin;
    private Nivel nivelMax;
    private Date fechaCreacion;
    private PartidoJugadores partidoJugadores;
    private PartidoEstadisticas partidoEstadisticas;

    public Partido(Deporte deporte, Usuario organizador, int jugadoresRequeridos, 
                   Location ubicacion, LocalDateTime fechaHora, int duracion, 
                   Nivel nivelMin, Nivel nivelMax) {
        this.deporte = deporte;
        this.organizador = organizador;
        this.jugadoresRequeridos = jugadoresRequeridos;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.nivelMin = nivelMin;
        this.nivelMax = nivelMax;
        this.fechaCreacion = new Date();
        this.partidoJugadores = new PartidoJugadores(jugadoresRequeridos);
        this.estado = new NecesitamosJugadores();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Deporte getDeporte() { return deporte; }
    public Usuario getOrganizador() { return organizador; }
    public int getJugadoresRequeridos() { return jugadoresRequeridos; }
    public PartidoState getEstado() { return estado; }
    public Location getUbicacion() { return ubicacion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public int getDuracion() { return duracion; }
    public Nivel getNivelMin() { return nivelMin; }
    public Nivel getNivelMax() { return nivelMax; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public PartidoJugadores getPartidoJugadores() { return partidoJugadores; }
    public PartidoEstadisticas getPartidoEstadisticas() { 
        if (partidoEstadisticas == null) {
            partidoEstadisticas = new PartidoEstadisticas();
        }
        return partidoEstadisticas; 
    }

    public void setEstado(PartidoState estado) {
        this.estado = estado;
    }

    public void cancelar() {
        if (estado != null) {
            estado.cancelar(this);
        }
    }

    public void confirmar() {
        if (estado != null) {
            estado.confirmar(this);
        }
    }
}

