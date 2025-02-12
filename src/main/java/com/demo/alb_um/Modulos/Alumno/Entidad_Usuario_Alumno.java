package com.demo.alb_um.Modulos.Alumno;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Antropometria.Ent_Antro;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import jakarta.persistence.*;
import java.util.Set;


@Entity
@Table(name = "usuario_alumno")
public class Entidad_Usuario_Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_alumno")
    private Long idUsuarioAlumno;

    @Column(name = "semestre", length = 10)
    private String semestre;

    @Column(name = "residencia", length = 20)
    private String residencia;

    @ManyToOne
    @JoinColumn(name = "id_carrera", nullable = true)
    private Entidad_carrera carrera;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Entidad_Usuario usuario;

    @OneToMany(mappedBy = "usuarioAlumno", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Cambiado a EAGER
    @JsonManagedReference
    private Set<Ent_AsistenciaActividadFisica> asistencias;    

    @OneToMany(mappedBy = "usuarioAlumno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_AlumnoActividad> alumnoActividades;

    @OneToMany(mappedBy = "usuarioAlumno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_InscripcionTaller> inscripcionTalleres;

    @OneToMany(mappedBy = "usuarioAlumno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_Cita> citas;

    @OneToMany(mappedBy = "usuarioAlumno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ent_Antro> datosAntropometricos;


    // Getters and Setters




    public Entidad_carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Entidad_carrera carrera) {
        this.carrera = carrera;
    }

    public Long getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(Long idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }


    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public Entidad_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Entidad_Usuario usuario) {
        this.usuario = usuario;
    }


    public Set<Ent_AsistenciaActividadFisica> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Set<Ent_AsistenciaActividadFisica> asistencias) {
        this.asistencias = asistencias;
    }

    public Set<Ent_AlumnoActividad> getAlumnoActividades() {
        return alumnoActividades;
    }

    public void setAlumnoActividades(Set<Ent_AlumnoActividad> alumnoActividades) {
        this.alumnoActividades = alumnoActividades;
    }

    public Set<Ent_InscripcionTaller> getInscripcionTalleres() {
        return inscripcionTalleres;
    }

    public void setInscripcionTalleres(Set<Ent_InscripcionTaller> inscripcionTalleres) {
        this.inscripcionTalleres = inscripcionTalleres;
    }

    public Set<Ent_Cita> getCitas() {
        return citas;
    }

    public void setCitas(Set<Ent_Cita> citas) {
        this.citas = citas;
    }
}
