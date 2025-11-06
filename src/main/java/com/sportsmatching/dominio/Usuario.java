package com.sportsmatching.dominio;

import com.sportsmatching.dominio.catalogos.Deporte;
import com.sportsmatching.dominio.catalogos.Nivel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Nivel nivel;
    private Deporte deporteFavorito;
    private Location ubicacion;

    public Usuario(String username, String email, String password, Nivel nivel, Deporte deporteFavorito, Location ubicacion) {
        this.username = username;
        this.email = email;
        this.password = password; // En producción debería ser hash
        this.nivel = nivel;
        this.deporteFavorito = deporteFavorito;
        this.ubicacion = ubicacion;
    }

    public Usuario(Long id, String username, String email, String password, Nivel nivel, Deporte deporteFavorito, Location ubicacion) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nivel = nivel;
        this.deporteFavorito = deporteFavorito;
        this.ubicacion = ubicacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Nivel getNivel() { return nivel; }
    public Deporte getDeporteFavorito() { return deporteFavorito; }
    public Location getUbicacion() { return ubicacion; }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }
}

