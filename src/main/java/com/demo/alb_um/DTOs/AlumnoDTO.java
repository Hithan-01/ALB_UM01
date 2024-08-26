package com.demo.alb_um.DTOs;

public class AlumnoDTO {
    private String nombreCompleto;
    private String nombreActividadFisica;
    private String nombreCoach;
    private String horario;

    // Constructor
    public AlumnoDTO(String nombreCompleto, String nombreActividadFisica, String nombreCoach, String horario) {
        this.nombreCompleto = nombreCompleto;
        this.nombreActividadFisica = nombreActividadFisica;
        this.nombreCoach = nombreCoach;
        this.horario = horario;
    }

    // Getters and setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreActividadFisica() {
        return nombreActividadFisica;
    }

    public void setNombreActividadFisica(String nombreActividadFisica) {
        this.nombreActividadFisica = nombreActividadFisica;
    }

    public String getNombreCoach() {
        return nombreCoach;
    }

    public void setNombreCoach(String nombreCoach) {
        this.nombreCoach = nombreCoach;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
