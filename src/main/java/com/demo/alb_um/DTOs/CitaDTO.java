package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {
    
    private LocalDate diaSemana;
    private LocalTime hora;
    private String estadoAsistencia;
    private Boolean verificacion;
    private String autorizadoPor;

    public CitaDTO(LocalDate diaSemana, LocalTime hora, String estadoAsistencia, Boolean verificacion, String autorizadoPor) {
        
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.estadoAsistencia = estadoAsistencia;
        this.verificacion = verificacion;
        this.autorizadoPor = autorizadoPor;
    }

    

    public LocalDate getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public Boolean getVerificacion() {
        return verificacion;
    }

    public String getAutorizadoPor() {
        return autorizadoPor;
    }

   

    public void setDiaSemana(LocalDate diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }

    public void setAutorizadoPor(String autorizadoPor) {
        this.autorizadoPor = autorizadoPor;
    }
}
