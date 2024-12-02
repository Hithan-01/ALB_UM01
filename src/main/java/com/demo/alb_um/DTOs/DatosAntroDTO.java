package com.demo.alb_um.DTOs;

public class DatosAntroDTO {
    private Long idDatos;
    private Double peso;
    private Double talla;
    private Double imc;
    private Double cintura;
    private Double porcentajeGrasa;
    private Double porcentajeMusculo;
    private Integer edadMetabolica;
    private String dieta;
    private String nombreAlumno;

    // Getters y setters

    public Long getIdDatos() {
        return idDatos;
    }

    public void setIdDatos(Long idDatos) {
        this.idDatos = idDatos;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getTalla() {
        return talla;
    }

    public void setTalla(Double talla) {
        this.talla = talla;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Double getCintura() {
        return cintura;
    }

    public void setCintura(Double cintura) {
        this.cintura = cintura;
    }

    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }

    public Double getPorcentajeMusculo() {
        return porcentajeMusculo;
    }

    public void setPorcentajeMusculo(Double porcentajeMusculo) {
        this.porcentajeMusculo = porcentajeMusculo;
    }

    public Integer getEdadMetabolica() {
        return edadMetabolica;
    }

    public void setEdadMetabolica(Integer edadMetabolica) {
        this.edadMetabolica = edadMetabolica;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }
}
