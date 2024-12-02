package com.demo.alb_um.Modulos.Antropometria;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;

import jakarta.persistence.*;

@Entity
@Table(name = "datos_antropometricos")
public class Ent_Antro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos")
    private Long idDatos;

    @OneToOne
    @JoinColumn(name = "id_cita")
    private Ent_Cita cita;

    @ManyToOne
    @JoinColumn(name = "id_usuario_alumno")
    private Entidad_Usuario_Alumno usuarioAlumno;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "talla")
    private Double talla;

    @Column(name = "cintura")
    private Double cintura;

    @Column(name = "porcentaje_grasa")
    private Double porcentajeGrasa;

    @Column(name = "porcentaje_musculo")
    private Double porcentajeMusculo;

    @Column(name = "imc")
    private Double imc;

    @Column(name = "edad_metabolica")
    private Integer edadMetabolica;

    @Column(name = "dieta")
    private String dieta;

    public Long getIdDatos() {
        return idDatos;
    }
    
    public void setIdDatos(Long idDatos) {
        this.idDatos = idDatos;
    }
    
    public Ent_Cita getCita() {
        return cita;
    }
    
    public void setCita(Ent_Cita cita) {
        this.cita = cita;
    }
    
    public Entidad_Usuario_Alumno getUsuarioAlumno() {
        return usuarioAlumno;
    }
    
    public void setUsuarioAlumno(Entidad_Usuario_Alumno usuarioAlumno) {
        this.usuarioAlumno = usuarioAlumno;
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
    
    public Double getImc() {
        return imc;
    }
    
    public void setImc(Double imc) {
        this.imc = imc;
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
    
}