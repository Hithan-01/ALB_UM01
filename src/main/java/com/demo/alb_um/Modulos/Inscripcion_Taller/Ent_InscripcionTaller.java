package com.demo.alb_um.Modulos.Inscripcion_Taller;
import jakarta.persistence.*;

import java.time.LocalTime;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "inscripcion_taller")
public class Ent_InscripcionTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;

    @ManyToOne
    @JoinColumn(name = "id_taller")
    @JsonBackReference
    private Ent_Taller taller;

    @ManyToOne
    @JoinColumn(name = "id_usuario_alumno")
    @JsonBackReference
    private Entidad_Usuario_Alumno usuarioAlumno;

    @Column(name = "estado_asistencia", length = 20)
    private String estadoAsistencia;

    @Column(name = "verificacion")
    private Boolean verificacion;

    @Column(name = "hora_llegada")
    private LocalTime horaLlegada;  // Para comparar con la hora de inicio del taller

    @Column(name = "hora_salida")
    private LocalTime horaSalida;   // Para validar que completó el taller
    // Getters y Setters
    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Ent_Taller getTaller() {
        return taller;
    }

    public void setTaller(Ent_Taller taller) {
        this.taller = taller;
    }

    public Entidad_Usuario_Alumno getUsuarioAlumno() {
        return usuarioAlumno;
    }

    public void setUsuarioAlumno(Entidad_Usuario_Alumno usuarioAlumno) {
        this.usuarioAlumno = usuarioAlumno;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public Boolean getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    // Getter y Setter para horaSalida
    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}
