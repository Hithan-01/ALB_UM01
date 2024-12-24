package com.demo.alb_um.DTOs;

public class RegistrarActividadDTO {
    private String nombre;
    private String diaSemana;
    private String hora; 
    private String grupo;

    // Constructor
    public RegistrarActividadDTO(String nombre, String diaSemana, String hora, String grupo) {
        this.nombre = nombre;
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.grupo = grupo;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
