package com.demo.alb_um.DTOs;

import java.time.LocalDate;

public class RegistroAlumnoDTO {

    // Datos generales del Usuario
    private String userName;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String genero;
    private LocalDate fechaNacimiento;
    private String tagCredencial;

    // Datos específicos del Alumno
    private String semestre;
    private String facultad;
    private String residencia;
    private String carrera;

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

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }

    public String getResidencia() { return residencia; }
    public void setResidencia(String residencia) { this.residencia = residencia; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String gettagCredencial(){ return tagCredencial;}
    public void settagCredencial(String tagCredencial) {this.tagCredencial = tagCredencial; }
}
