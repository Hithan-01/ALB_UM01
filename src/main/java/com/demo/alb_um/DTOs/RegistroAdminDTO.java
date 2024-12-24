package com.demo.alb_um.DTOs;

import java.time.LocalDate;

public class RegistroAdminDTO {

    // Datos generales del Usuario
    private String userName;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String genero;
    private LocalDate fechaNacimiento;
    private String tagCredencial;

    // Datos específicos del Admin
    private String cargoServicio;
    private Long servicioId; // ID del servicio al que está asignado

    // Getters y Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getCargoServicio() { return cargoServicio; }
    public void setCargoServicio(String cargoServicio) { this.cargoServicio = cargoServicio; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public String gettagCredencial(){ return tagCredencial;}
    public void settagCredencial(String tagCredencial) {this.tagCredencial = tagCredencial; }
}
