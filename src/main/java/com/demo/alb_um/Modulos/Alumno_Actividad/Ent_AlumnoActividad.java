package com.demo.alb_um.Modulos.Alumno_Actividad;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "alumno_actividad")
public class Ent_AlumnoActividad {

    @EmbeddedId
    private AlumnoActividadId id;

    @ManyToOne
    @JoinColumn(name = "id_usuario_alumno", insertable = false, updatable = false)
    @JsonBackReference
    private Entidad_Usuario_Alumno usuarioAlumno;

    @ManyToOne
    @JoinColumn(name = "id_actividad_fisica", insertable = false, updatable = false)
    @JsonBackReference
    private Entidad_ActividadFisica actividadFisica;

    // Getters y Setters

    public AlumnoActividadId getId() {
        return id;
    }

    public void setId(AlumnoActividadId id) {
        this.id = id;
    }

    public Entidad_Usuario_Alumno getUsuarioAlumno() {
        return usuarioAlumno;
    }

    public void setUsuarioAlumno(Entidad_Usuario_Alumno usuarioAlumno) {
        this.usuarioAlumno = usuarioAlumno;
    }

    public Entidad_ActividadFisica getActividadFisica() {
        return actividadFisica;
    }

    public void setActividadFisica(Entidad_ActividadFisica actividadFisica) {
        this.actividadFisica = actividadFisica;
    }
}
