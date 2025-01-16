package com.demo.alb_um.Modulos.Carrera;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Facultad.Entidad_facultad;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "carrera")
public class Entidad_carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera")
    private Long idCarrera;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_facultad", nullable = false)
    private Entidad_facultad facultad;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entidad_Usuario_Alumno> alumnos;

    // Getters y Setters
    public Long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Long idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Entidad_facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Entidad_facultad facultad) {
        this.facultad = facultad;
    }

    public Set<Entidad_Usuario_Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Entidad_Usuario_Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
