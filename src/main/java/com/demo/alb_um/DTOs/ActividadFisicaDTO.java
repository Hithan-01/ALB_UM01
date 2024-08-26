package com.demo.alb_um.DTOs;

import java.sql.Time;
import java.util.List;

public class ActividadFisicaDTO {
    private Long idActividadFisica;
    private String nombre;
    private String grupo;
    private String diaSemana;
    private Time hora;
    private List<AlumnoDTO> alumnos;

    // Constructor
    public ActividadFisicaDTO(Long idActividadFisica, String nombre, String grupo, String diaSemana, Time hora, List<AlumnoDTO> alumnos) {
        this.idActividadFisica = idActividadFisica;
        this.nombre = nombre;
        this.grupo = grupo;
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.alumnos = alumnos;
    }

    // Getters y Setters
    public Long getIdActividadFisica() {
        return idActividadFisica;
    }

    public void setIdActividadFisica(Long idActividadFisica) {
        this.idActividadFisica = idActividadFisica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public List<AlumnoDTO> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoDTO> alumnos) {
        this.alumnos = alumnos;
    }
}
