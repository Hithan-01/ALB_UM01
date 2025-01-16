package com.demo.alb_um.Modulos.Facultad;

import jakarta.persistence.*;
import java.util.Set;

import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;

@Entity
@Table(name = "facultad")
public class Entidad_facultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facultad")
    private Long idFacultad;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entidad_carrera> carreras;

    // Getters y Setters
    public Long getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(Long idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Entidad_carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(Set<Entidad_carrera> carreras) {
        this.carreras = carreras;
    }
}
