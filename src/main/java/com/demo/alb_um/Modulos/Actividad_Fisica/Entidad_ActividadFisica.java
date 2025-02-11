package com.demo.alb_um.Modulos.Actividad_Fisica;

import jakarta.persistence.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Set;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "actividad_fisica")
public class Entidad_ActividadFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad_fisica")
    private Long idActividadFisica;

    @Column(name = "nombre", length = 30)
    private String nombre;

    @Column(name = "hora")
    @JsonFormat(pattern = "hh:mm a")
    @Temporal(TemporalType.TIME)
    private Time hora;

    @Column(name = "grupo", length = 10)
    private String grupo;

    @Column(name = "dia_semana", length = 15)
    private String diaSemana;

    @Column(name = "identificador_grupo", length = 10)
    private String identificadorGrupo;

    @OneToMany(mappedBy = "actividadFisica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Entidad_Lista> listas;

    @OneToMany(mappedBy = "actividadFisica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_CoachActividad> coachActividades;

    @OneToMany(mappedBy = "actividadFisica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_AlumnoActividad> alumnoActividades;

    // Constructor por defecto
    public Entidad_ActividadFisica() {
    }

    // Getters y Setters
    public Long getIdActividadFisica() {
        return idActividadFisica;
    }

    public void setIdActividadFisica(Long idActividadFisica) {
        this.idActividadFisica = idActividadFisica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        // Asegurarse de que la hora está en el formato correcto
        if (hora != null) {
            LocalTime localTime = hora.toLocalTime();
            this.hora = Time.valueOf(localTime);
        } else {
            this.hora = null;
        }
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Set<Entidad_Lista> getListas() {
        return listas;
    }

    public void setListas(Set<Entidad_Lista> listas) {
        this.listas = listas;
    }

    public Set<Ent_CoachActividad> getCoachActividades() {
        return coachActividades;
    }

    public void setCoachActividades(Set<Ent_CoachActividad> coachActividades) {
        this.coachActividades = coachActividades;
    }

    public Set<Ent_AlumnoActividad> getAlumnoActividades() {
        return alumnoActividades;
    }

    public void setAlumnoActividades(Set<Ent_AlumnoActividad> alumnoActividades) {
        this.alumnoActividades = alumnoActividades;
    }

    public String getIdentificadorGrupo() {
        return identificadorGrupo;
    }

    public void setIdentificadorGrupo(String identificadorGrupo) {
        this.identificadorGrupo = identificadorGrupo;
    }

    // Método para formatear la hora en formato 12 horas
    @Transient
    public String getHoraFormateada() {
        if (hora != null) {
            LocalTime localTime = hora.toLocalTime();
            return localTime.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
        }
        return null;
    }
}