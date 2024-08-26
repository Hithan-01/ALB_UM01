package com.demo.alb_um.Modulos.Horario_servicio;

import jakarta.persistence.*;
import java.util.Set;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "horario_servicio")
public class Ent_HorarioServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_servicio")
    private Long idHorarioServicio;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    @JsonBackReference
    private Ent_Servicio servicio;

    @Column(name = "dia_semana")
    private LocalDate diaSemana;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "disponible")
    private Boolean disponible;

    @OneToMany(mappedBy = "horarioServicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_Cita> citas;

    // Getters y Setters

    public Long getIdHorarioServicio() {
        return idHorarioServicio;
    }

    public void setIdHorarioServicio(Long idHorarioServicio) {
        this.idHorarioServicio = idHorarioServicio;
    }

    public Ent_Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Ent_Servicio servicio) {
        this.servicio = servicio;
    }

    public LocalDate getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(LocalDate diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Ent_Cita> getCitas() {
        return citas;
    }

    public void setCitas(Set<Ent_Cita> citas) {
        this.citas = citas;
    }
}
