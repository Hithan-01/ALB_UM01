package com.demo.alb_um.DTOs;

import java.util.List;

public class CoachDTO {
    private String nombreCompleto;
    private String email;
    private List<ActividadFisicaDTO> actividades;

    // Constructor
    public CoachDTO(String nombreCompleto, String email, List<ActividadFisicaDTO> actividades) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.actividades = actividades;
    }

    // Getters y Setters
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

    public List<ActividadFisicaDTO> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadFisicaDTO> actividades) {
        this.actividades = actividades;
    }
}
