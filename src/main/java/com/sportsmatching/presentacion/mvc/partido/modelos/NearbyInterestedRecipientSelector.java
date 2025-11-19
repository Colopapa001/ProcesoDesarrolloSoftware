package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.aplicacion.servicios.DistanceCalculator;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.infraestructura.persistence.UsuarioRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NearbyInterestedRecipientSelector implements PartidoRecipientSelector {
    private final UsuarioRepository usuarioRepository;
    private final DistanceCalculator distanceCalculator;
    private final double distanciaMaximaKm;

    public NearbyInterestedRecipientSelector(UsuarioRepository usuarioRepository,
                                             DistanceCalculator distanceCalculator,
                                             double distanciaMaximaKm) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "usuarioRepository");
        this.distanceCalculator = Objects.requireNonNull(distanceCalculator, "distanceCalculator");
        this.distanciaMaximaKm = distanciaMaximaKm;
    }

    @Override
    public List<PartidoDestinatario> seleccionarDestinatarios(Partido partido) {
        Collection<Usuario> todosUsuarios = usuarioRepository.findAll();

        return todosUsuarios.stream()
            .filter(usuario -> usuario.getId() != null && !usuario.getId().equals(partido.getOrganizador().getId()))
            .filter(usuario -> usuario.getUbicacion() != null && partido.getUbicacion() != null)
            .map(usuario -> {
                double distancia = distanceCalculator.calcularDistancia(usuario.getUbicacion(), partido.getUbicacion());
                return new PartidoDestinatario(usuario, distancia);
            })
            .filter(destinatario -> destinatario.getDistanciaKm() <= distanciaMaximaKm)
            .filter(destinatario -> destinatario.getUsuario().getDeporteFavorito() != null &&
                    destinatario.getUsuario().getDeporteFavorito().getId().equals(partido.getDeporte().getId()))
            .filter(destinatario -> destinatario.getUsuario().getNivel() != null &&
                    estaDentroDelRango(partido, destinatario.getUsuario().getNivel().getId()))
            .collect(Collectors.toList());
    }

    private boolean estaDentroDelRango(Partido partido, Long nivelUsuarioId) {
        if (partido.getNivelMin() == null || partido.getNivelMax() == null) {
            return true;
        }
        return nivelUsuarioId >= partido.getNivelMin().getId() &&
               nivelUsuarioId <= partido.getNivelMax().getId();
    }
}

