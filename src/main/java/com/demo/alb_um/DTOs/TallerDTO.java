package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class TallerDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private String estadoAsistencia;

    public TallerDTO(String nombre, String descripcion, LocalDate fecha, LocalTime hora, String estadoAsistencia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoAsistencia = estadoAsistencia;
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
}
