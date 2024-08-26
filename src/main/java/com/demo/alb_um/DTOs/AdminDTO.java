package com.demo.alb_um.DTOs;

import java.util.List;

public class AdminDTO {
    private String nombreCompleto;
    private String cargoServicio;
    private List<CitaDTO> citas;

    // Constructor
    public AdminDTO(String nombreCompleto, String cargoServicio, List<CitaDTO> citas) {
        this.nombreCompleto = nombreCompleto;
        this.cargoServicio = cargoServicio;
        this.citas = citas;
    }

    // Getters and Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCargoServicio() {
        return cargoServicio;
    }

    public void setCargoServicio(String cargoServicio) {
        this.cargoServicio = cargoServicio;
    }

    public List<CitaDTO> getCitas() {
        return citas;
    }

    public void setCitas(List<CitaDTO> citas) {
        this.citas = citas;
    }
}
