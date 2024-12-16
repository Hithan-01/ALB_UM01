package com.demo.alb_um.DTOs;

public class CoachDTO {
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    private String username; // Matrícula o username del Coach
    private String rol; // Nuevo campo para rol

    // Constructor
    public CoachDTO(Long idUsuario, String nombreCompleto, String email, String username, String rol) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.username = username;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
