package com.demo.alb_um.DTOs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BusquedaFaltas {

    private Long id; 
    private String nombreCompleto; // Nombre y apellido del alumno
    private String matricula; // Matrícula del alumno
    private String carrera; // Nombre de la carrera
    private String facultad; // Nombre de la facultad
    private Date fechaFalta; // Fecha de la falta
    private String nombreActividad; // Nombre de la actividad o clase
    private String semestre; // Semestre del alumno

    // Constructor
    public BusquedaFaltas(Long id, String nombreCompleto, String matricula, String carrera, String facultad, Date fechaFalta, String nombreActividad, String semestre) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
        this.carrera = carrera;
        this.facultad = facultad;
        this.fechaFalta = fechaFalta;
        this.nombreActividad = nombreActividad;
        this.semestre = semestre;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public Date getFechaFalta() {
        return fechaFalta;
    }

    public void setFechaFalta(Date fechaFalta) {
        this.fechaFalta = fechaFalta;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getFechaFaltaFormateada() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    return fechaFalta != null ? formatter.format(fechaFalta) : "Sin fecha";
}

}
