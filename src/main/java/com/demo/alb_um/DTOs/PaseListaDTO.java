package com.demo.alb_um.DTOs;


import java.util.List;

import com.demo.alb_um.Modulos.Listas.Entidad_Lista;

public class PaseListaDTO {
    private Entidad_Lista lista;
    private ActividadFisicaDTO actividad;
    private List<AlumnoDTO> alumnos;

    public PaseListaDTO(Entidad_Lista lista, ActividadFisicaDTO actividad, List<AlumnoDTO> alumnos) {
        this.lista = lista;
        this.actividad = actividad;
        this.alumnos = alumnos;
    }

    // Getters
    public Entidad_Lista getLista() {
        return lista;
    }

    public ActividadFisicaDTO getActividad() {
        return actividad;
    }

    public List<AlumnoDTO> getAlumnos() {
        return alumnos;
    }

    // Setters
    public void setLista(Entidad_Lista lista) {
        this.lista = lista;
    }

    public void setActividad(ActividadFisicaDTO actividad) {
        this.actividad = actividad;
    }

    public void setAlumnos(List<AlumnoDTO> alumnos) {
        this.alumnos = alumnos;
    }
}