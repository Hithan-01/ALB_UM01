package com.demo.alb_um.DTOs;

import java.util.List;

import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;

public class AdminDTO {
    private String nombreCompleto;
    private String cargoServicio;
    Ent_Servicio servicio;
    private List<CitaDTO> citas;

    // Constructor
    public AdminDTO(String nombreCompleto, String cargoServicio,Ent_Servicio servicio, List<CitaDTO> citas) {
        this.nombreCompleto = nombreCompleto;
        this.cargoServicio = cargoServicio;
        this.citas = citas;
        this.servicio = servicio;
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

    public Ent_Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Ent_Servicio servicio) {
        this.servicio = servicio;
    }

      // Sobrescribir el método toString
      @Override
      public String toString() {
          return "AdminDTO{" +
                  "nombreCompleto='" + nombreCompleto + '\'' +
                  ", cargoServicio='" + cargoServicio + '\'' +
                  ", servicio=" + (servicio != null ? servicio.getNombre() : "null") +
                  ", citas=" + citas +
                  '}';
      }
}
