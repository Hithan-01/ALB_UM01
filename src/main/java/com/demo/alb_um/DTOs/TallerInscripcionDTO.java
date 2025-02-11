package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class TallerInscripcionDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private String estadoAsistencia;
    private String lugar; // 🔹 Se agrega el campo "lugar"

    public TallerInscripcionDTO(String nombre, String descripcion, LocalDate fecha, LocalTime hora, String estadoAsistencia, String lugar) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoAsistencia = estadoAsistencia;
        this.lugar = lugar; // 🔹 Se inicializa el campo "lugar"
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public String getLugar() { // 🔹 Nuevo getter para "lugar"
        return lugar;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public void setLugar(String lugar) { // 🔹 Nuevo setter para "lugar"
        this.lugar = lugar;
    }
}
