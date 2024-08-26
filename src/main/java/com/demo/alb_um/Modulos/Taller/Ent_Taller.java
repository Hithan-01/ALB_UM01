package com.demo.alb_um.Modulos.Taller;

import jakarta.persistence.*;
import java.util.Set;

import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "taller")
public class Ent_Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taller")
    private Long idTaller;

    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha")
    private java.sql.Date fecha;

    @Column(name = "hora")
    private java.sql.Time hora;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "cupos")
    private Integer cupos;

    @Column(name = "cupos_disponibles")
    private Integer cuposDisponibles;

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_InscripcionTaller> inscripciones;

    // Getters y Setters
    public Long getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Long idTaller) {
        this.idTaller = idTaller;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public java.sql.Time getHora() {
        return hora;
    }

    public void setHora(java.sql.Time hora) {
        this.hora = hora;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public Set<Ent_InscripcionTaller> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(Set<Ent_InscripcionTaller> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
