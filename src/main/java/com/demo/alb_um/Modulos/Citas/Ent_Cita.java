package com.demo.alb_um.Modulos.Citas;

import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import jakarta.persistence.*;

@Entity
@Table(name = "citas")
public class Ent_Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "id_horario_servicio")
    @JsonBackReference
    private Ent_HorarioServicio horarioServicio;

    @ManyToOne
    @JoinColumn(name = "id_usuario_admin")
    @JsonBackReference
    private Ent_UsuarioAdmin usuarioAdmin;

    @ManyToOne
    @JoinColumn(name = "id_usuario_alumno")
    @JsonBackReference
    private Entidad_Usuario_Alumno usuarioAlumno;

    @Column(name = "estado_asistencia", length = 20)
    private String estadoAsistencia;

    @Column(name = "verificacion")
    private Boolean verificacion;

    @Column(name = "autorizado_por", length = 100)
    private String autorizadoPor;

    // Getters y Setters

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public Ent_HorarioServicio getHorarioServicio() {
        return horarioServicio;
    }

    public void setHorarioServicio(Ent_HorarioServicio horarioServicio) {
        this.horarioServicio = horarioServicio;
    }

    public Ent_UsuarioAdmin getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(Ent_UsuarioAdmin usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
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

    public String getAutorizadoPor() {
        return autorizadoPor;
    }

    public void setAutorizadoPor(String autorizadoPor) {
        this.autorizadoPor = autorizadoPor;
    }
}
