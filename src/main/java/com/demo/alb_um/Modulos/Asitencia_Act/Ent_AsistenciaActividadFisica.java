package com.demo.alb_um.Modulos.Asitencia_Act;
import java.time.LocalDateTime;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "asistencia_actividad_fisica")
public class Ent_AsistenciaActividadFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia_actividad_fisica")
    private Long idAsistenciaActividadFisica;
    


@Column(name = "fecha_registro")
private LocalDateTime fechaRegistro;

@Column(name = "justificado_por")
private String justificadoPor;

@Column(name = "fecha_justificacion")
private LocalDateTime fechaJustificacion;

@Column(name = "comentario_justificacion")
private String comentarioJustificacion;



@Enumerated(EnumType.STRING)
@Column(name = "estado_falta", nullable = false)
private EstadoFalta estadoFalta = EstadoFalta.FALTA; // Valor por defecto

    @ManyToOne
    @JoinColumn(name = "id_lista")
    @JsonBackReference
    private Entidad_Lista lista;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario_alumno", nullable = false)
    @JsonBackReference
    private Entidad_Usuario_Alumno usuarioAlumno;

    // Getters y Setters

    public Long getIdAsistenciaActividadFisica() {
        return idAsistenciaActividadFisica;
    }

    public void setIdAsistenciaActividadFisica(Long idAsistenciaActividadFisica) {
        this.idAsistenciaActividadFisica = idAsistenciaActividadFisica;
    }

    public Entidad_Lista getLista() {
        return lista;
    }

    public void setLista(Entidad_Lista lista) {
        this.lista = lista;
    }
    
    public Entidad_Usuario_Alumno getUsuarioAlumno() {
        return usuarioAlumno;
    }

    public void setUsuarioAlumno(Entidad_Usuario_Alumno usuarioAlumno) {
       
        this.usuarioAlumno = usuarioAlumno;
    }


    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EstadoFalta getEstadoFalta() {
        return estadoFalta;
    }

    public void setEstadoFalta(EstadoFalta estadoFalta) {
        this.estadoFalta = estadoFalta;
    }

    public enum EstadoFalta {
        PRESENTE,
        FALTA,
        JUSTIFICADA
    }

    public String getJustificadoPor() {
        return justificadoPor;
    }

    public void setJustificadoPor(String justificadoPor) {
        this.justificadoPor = justificadoPor;
    }

    public LocalDateTime getFechaJustificacion() {
        return fechaJustificacion;
    }

    public void setFechaJustificacion(LocalDateTime fechaJustificacion) {
        this.fechaJustificacion = fechaJustificacion;
    }

    public String getComentarioJustificacion() {
        return comentarioJustificacion;
    }

    public void setComentarioJustificacion(String comentarioJustificacion) {
        this.comentarioJustificacion = comentarioJustificacion;
    }
    
}
