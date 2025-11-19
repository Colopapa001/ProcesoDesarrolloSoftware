package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;

import java.util.List;

public interface PartidoRecipientSelector {
    List<PartidoDestinatario> seleccionarDestinatarios(Partido partido);
}

