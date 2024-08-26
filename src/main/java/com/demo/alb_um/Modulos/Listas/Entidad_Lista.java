package com.demo.alb_um.Modulos.Listas;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "lista")
public class Entidad_Lista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Long idLista;

    @Column(name = "fecha")
    private Date fecha;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ent_AsistenciaActividadFisica> asistencias;

    @ManyToOne
    @JoinColumn(name = "actividad_fisica_id")
    private Entidad_ActividadFisica actividadFisica;

    // Getters y Setters

    public Long getIdLista() {
        return idLista;
    }

    public void setIdLista(Long idLista) {
        this.idLista = idLista;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Set<Ent_AsistenciaActividadFisica> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Set<Ent_AsistenciaActividadFisica> asistencias) {
        this.asistencias = asistencias;
    }


    public Entidad_ActividadFisica getActividadFisica() {
        return actividadFisica;
    }

    public void setActividadFisica(Entidad_ActividadFisica actividadFisica) {
        this.actividadFisica = actividadFisica;
    }
}
