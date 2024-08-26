package com.demo.alb_um.Modulos.Coach;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CoachActividadId implements Serializable {
    private Long usuarioId;
    private Long actividadFisicaId;

    // Constructores, Getters, Setters, equals() y hashCode()

    public CoachActividadId() {}

    public CoachActividadId(Long usuarioId, Long actividadFisicaId) {
        this.usuarioId = usuarioId;
        this.actividadFisicaId = actividadFisicaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getActividadFisicaId() {
        return actividadFisicaId;
    }

    public void setActividadFisicaId(Long actividadFisicaId) {
        this.actividadFisicaId = actividadFisicaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoachActividadId that = (CoachActividadId) o;
        return Objects.equals(usuarioId, that.usuarioId) &&
               Objects.equals(actividadFisicaId, that.actividadFisicaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, actividadFisicaId);
    }
}
