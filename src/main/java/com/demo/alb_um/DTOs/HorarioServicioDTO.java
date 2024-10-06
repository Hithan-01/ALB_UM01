package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class HorarioServicioDTO {

    private Long idHorarioServicio;
    private Long servicioId;
    private LocalDate diaSemana;
    private LocalTime hora;
    private Boolean disponible;
    private Set<Long> citasIds;

    public HorarioServicioDTO(Long idHorarioServicio, Long servicioId, LocalDate diaSemana, LocalTime hora, Boolean disponible) {
        this.idHorarioServicio = idHorarioServicio;
        this.servicioId = servicioId;
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.disponible = disponible;
    }

    public Long getIdHorarioServicio() {
        return idHorarioServicio;
    }

    public void setIdHorarioServicio(Long idHorarioServicio) {
        this.idHorarioServicio = idHorarioServicio;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDate getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(LocalDate diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Long> getCitasIds() {
        return citasIds;
    }

    public void setCitasIds(Set<Long> citasIds) {
        this.citasIds = citasIds;
    }
}
