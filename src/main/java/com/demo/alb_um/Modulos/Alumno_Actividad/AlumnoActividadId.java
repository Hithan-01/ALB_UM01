package com.demo.alb_um.Modulos.Alumno_Actividad;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlumnoActividadId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_usuario_alumno")
    private Long idUsuarioAlumno;

    @Column(name = "id_actividad_fisica")
    private Long idActividadFisica;

    // Constructor, Getters y Setters

    public AlumnoActividadId() {
    }

    public AlumnoActividadId(Long idUsuarioAlumno, Long idActividadFisica) {
        this.idUsuarioAlumno = idUsuarioAlumno;
        this.idActividadFisica = idActividadFisica;
    }

    public Long getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(Long idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }

    public Long getIdActividadFisica() {
        return idActividadFisica;
    }

    public void setIdActividadFisica(Long idActividadFisica) {
        this.idActividadFisica = idActividadFisica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlumnoActividadId that = (AlumnoActividadId) o;
        return Objects.equals(idUsuarioAlumno, that.idUsuarioAlumno) &&
                Objects.equals(idActividadFisica, that.idActividadFisica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuarioAlumno, idActividadFisica);
    }
}
