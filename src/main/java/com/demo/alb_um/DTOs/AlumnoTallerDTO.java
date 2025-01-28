package com.demo.alb_um.DTOs;

import java.time.LocalTime;

public class AlumnoTallerDTO {
    private Long idInscripcion;
    private String nombreCompleto;
    private String userName;
    private String carrera;
    private String facultad;
    private LocalTime horaLlegada; // Asegúrate de incluir esta propiedad
    private LocalTime horaSalida;  // Asegúrate de incluir esta propiedad
    private String estadoAsistencia;

    public AlumnoTallerDTO() {
    }
    
    // Constructor
    public AlumnoTallerDTO(Long idInscripcion, String nombreCompleto, String userName, String carrera, String facultad, LocalTime horaLlegada, LocalTime horaSalida, String estadoAsistencia) {
        this.idInscripcion = idInscripcion;
        this.nombreCompleto = nombreCompleto;
        this.userName = userName;
        this.carrera = carrera;
        this.facultad = facultad;
        this.horaLlegada = horaLlegada;
        this.horaSalida = horaSalida;
        this.estadoAsistencia = estadoAsistencia;

    }

    // Getters y Setters
    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }
}
