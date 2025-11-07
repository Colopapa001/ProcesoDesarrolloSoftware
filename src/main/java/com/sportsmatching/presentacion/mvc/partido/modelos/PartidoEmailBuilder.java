package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;

public interface PartidoEmailBuilder {
    EmailContent construirEmailPartidoCercano(Partido partido, Usuario destinatario, double distanciaKm);
}

