package com.demo.alb_um.Modulos.Coach;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import jakarta.persistence.*;

@Entity
@Table(name = "coach_actividad")
public class Ent_CoachActividad {

    @EmbeddedId
    private CoachActividadId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Entidad_Usuario usuario;

    @ManyToOne
    @MapsId("actividadFisicaId")
    @JoinColumn(name = "actividad_fisica_id")
     @JsonBackReference
    private Entidad_ActividadFisica actividadFisica;

    // Getters y Setters

    public CoachActividadId getId() {
        return id;
    }

    public void setId(CoachActividadId id) {
        this.id = id;
    }

    public Entidad_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Entidad_Usuario usuario) {
        this.usuario = usuario;
    }

    public Entidad_ActividadFisica getActividadFisica() {
        return actividadFisica;
    }

    public void setActividadFisica(Entidad_ActividadFisica actividadFisica) {
        this.actividadFisica = actividadFisica;
    }
}