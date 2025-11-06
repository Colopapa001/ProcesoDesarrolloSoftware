package com.sportsmatching.presentacion.mvc.busqueda.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.presentacion.mvc.busqueda.servicios.BusquedaFiltroService;
import com.sportsmatching.infraestructura.persistence.PartidoRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BusquedaModel {
    private final PartidoRepository partidoRepository;
    private final BusquedaFiltroService filtroService;

    public BusquedaModel(PartidoRepository partidoRepository, BusquedaFiltroService filtroService) {
        this.partidoRepository = partidoRepository;
        this.filtroService = filtroService;
    }

    public List<Partido> buscarPartidos(Map<String, Object> criterios) {
        List<Partido> todos = partidoRepository.buscarTodos();
        return filtroService.aplicarFiltros(todos, criterios);
    }
}

