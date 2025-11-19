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
        // Validar datos básicos (lanzará excepción con mensaje específico si falla)
        validacionService.validarDatos(username, email, password);
        
        // Validar unicidad (lanzará excepción con mensaje específico si falla)
        validacionService.validarUnicidad(email, username);
        
        // Validar ubicación (la validación de coordenadas se hace en el constructor de Location)
        validacionService.validarUbicacion(ubicacion);

        Usuario usuario = new Usuario(username, email, password, nivel, deporteFavorito, ubicacion);
        return usuarioRepository.save(usuario);
    }
}

