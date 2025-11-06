package com.sportsmatching.presentacion.mvc.registro.modelos;

import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.presentacion.mvc.registro.servicios.UsuarioValidacionService;

public class RegistroModel {
    private final UsuarioValidacionService validacionService;
    private final com.sportsmatching.infraestructura.persistence.UsuarioRepository usuarioRepository;

    public RegistroModel(UsuarioValidacionService validacionService,
                        com.sportsmatching.infraestructura.persistence.UsuarioRepository usuarioRepository) {
        this.validacionService = validacionService;
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(String username, String email, String password,
                               com.sportsmatching.dominio.catalogos.Nivel nivel,
                               com.sportsmatching.dominio.catalogos.Deporte deporteFavorito,
                               com.sportsmatching.dominio.Location ubicacion) {
        if (!validacionService.validarDatos(username, email, password)) {
            throw new IllegalArgumentException("Datos inválidos");
        }
        
        if (!validacionService.validarUnicidad(email, username)) {
            throw new IllegalArgumentException("Email o username ya existe");
        }

        Usuario usuario = new Usuario(username, email, password, nivel, deporteFavorito, ubicacion);
        return usuarioRepository.save(usuario);
    }
}

