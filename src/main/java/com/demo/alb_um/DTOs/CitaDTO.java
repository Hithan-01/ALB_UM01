package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {
    private Long id; // Campo para almacenar el ID de la cita
    private String nombreCita;
    private LocalDate diaSemana;
    private LocalTime hora;
    private String estadoAsistencia;
    private Boolean verificacion;
    private String autorizadoPor;
    private String nombreCompletoAlumno; // Nuevo campo para almacenar el nombre completo del alumno

    // Constructor
    public CitaDTO(Long id, String nombreCita, LocalDate diaSemana, LocalTime hora, String estadoAsistencia, Boolean verificacion, String autorizadoPor, String nombreCompletoAlumno) {
        this.id = id;
        this.nombreCita = nombreCita;
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.estadoAsistencia = estadoAsistencia;
        this.verificacion = verificacion;
        this.autorizadoPor = autorizadoPor;
        this.nombreCompletoAlumno = nombreCompletoAlumno;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCita() {
        return nombreCita;
    }

    public void setNombreCita(String nombreCita) {
        this.nombreCita = nombreCita;
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

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public Boolean getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }

    public String getAutorizadoPor() {
        return autorizadoPor;
    }

    public void setAutorizadoPor(String autorizadoPor) {
        this.autorizadoPor = autorizadoPor;
    }

    public String getNombreCompletoAlumno() {
        return nombreCompletoAlumno;
    }

    public void setNombreCompletoAlumno(String nombreCompletoAlumno) {
        this.nombreCompletoAlumno = nombreCompletoAlumno;
    }
}
