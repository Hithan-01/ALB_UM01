package com.demo.alb_um.DTOs;

import java.time.LocalDateTime;

public class AlumnoDTO {
    private Long idUsuarioAlumno;
    private String nombreCompleto;
    private String nombreActividadFisica;
    private String nombreCoach;
    private String horario;
    private boolean yaAsistio;
    private LocalDateTime fechaRegistro; 

    // Constructor
    public AlumnoDTO(Long idUsuarioAlumno, String nombreCompleto, String nombreActividadFisica, String nombreCoach, String horario, boolean yaAsistio, LocalDateTime fechaRegistro) {
        this.idUsuarioAlumno = idUsuarioAlumno;
        this.nombreCompleto = nombreCompleto;
        this.nombreActividadFisica = nombreActividadFisica;
        this.nombreCoach = nombreCoach;
        this.horario = horario;
        this.yaAsistio = yaAsistio;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters and setters
    public Long getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(Long idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }

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

    public boolean isYaAsistio() {
        return yaAsistio;
    }

    public void setYaAsistio(boolean yaAsistio) {
        this.yaAsistio = yaAsistio;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
