package com.demo.alb_um.Modulos.Caminata;

import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import jakarta.persistence.*;

@Entity
@Table(name = "caminata")
public class Entidad_Caminata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caminata")
    private Long idCaminata;

    @Column(name = "pasos_semanales", nullable = false)
    private int pasosSemanales;

    @Column(name = "semana_registro", nullable = false)
    private int semanaRegistro;

    @OneToOne
    @JoinColumn(name = "id_asistencia", referencedColumnName = "id_asistencia_actividad_fisica", nullable = false, unique = true)
    private Ent_AsistenciaActividadFisica asistencia;

    // Getters y Setters

    public Long getIdCaminata() {
        return idCaminata;
    }

    public void setIdCaminata(Long idCaminata) {
        this.idCaminata = idCaminata;
    }

    public int getPasosSemanales() {
        return pasosSemanales;
    }

    public void setPasosSemanales(int pasosSemanales) {
        this.pasosSemanales = pasosSemanales;
    }

    public int getSemanaRegistro() {
        return semanaRegistro;
    }

    public void setSemanaRegistro(int semanaRegistro) {
        this.semanaRegistro = semanaRegistro;
    }

    public Ent_AsistenciaActividadFisica getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Ent_AsistenciaActividadFisica asistencia) {
        this.asistencia = asistencia;
    }
}
