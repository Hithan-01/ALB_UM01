package com.demo.alb_um.DTOs;



public class AlumnoBusquedaDTO {

    private Long idAlumno;
    private String nombreCompleto;
    private String carrera;
    private String facultad;
    private Long contadorFaltas;        // Contador de faltas
    private Long contadorAsistencias;  // Contador de asistencias
    private Long contadorJustificaciones; // Contador de justificaciones
    private String semestre;
    private Long idActividadFisica; // Nuevo campo

    // Constructor actualizado
    public AlumnoBusquedaDTO(
            Long idAlumno,
            String nombreCompleto,
            String carrera,
            String facultad,
            Long contadorFaltas,
            Long contadorAsistencias,
            Long contadorJustificaciones,
            String semestre,
            Long idActividadFisica) {
        this.idAlumno = idAlumno;
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
        this.facultad = facultad;
        this.contadorFaltas = contadorFaltas;
        this.contadorAsistencias = contadorAsistencias;
        this.contadorJustificaciones = contadorJustificaciones;
        this.semestre = semestre;
        this.idActividadFisica = idActividadFisica;
    }

    // Getters y Setters
    public Long getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Long idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public Long getContadorFaltas() {
        return contadorFaltas;
    }

    public void setContadorFaltas(Long contadorFaltas) {
        this.contadorFaltas = contadorFaltas;
    }

    public Long getContadorAsistencias() {
        return contadorAsistencias;
    }

    public void setContadorAsistencias(Long contadorAsistencias) {
        this.contadorAsistencias = contadorAsistencias;
    }

    public Long getContadorJustificaciones() {
        return contadorJustificaciones;
    }

    public void setContadorJustificaciones(Long contadorJustificaciones) {
        this.contadorJustificaciones = contadorJustificaciones;
    }


    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Long getIdActividadFisica() {
        return idActividadFisica;
    }

    public void setIdActividadFisica(Long idActividadFisica) {
        this.idActividadFisica = idActividadFisica;
    }
}
