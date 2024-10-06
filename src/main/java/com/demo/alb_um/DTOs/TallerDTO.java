package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class TallerDTO {
    private Long idTaller; // Necesario para identificar el taller
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer cuposDisponibles;

    public TallerDTO(Long idTaller, String nombre, String descripcion, LocalDate fecha, LocalTime hora, Integer cuposDisponibles) {
        this.idTaller = idTaller;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.cuposDisponibles = cuposDisponibles;
    }

    // Getters y Setters
    public Long getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Long idTaller) {
        this.idTaller = idTaller;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
}
