package com.demo.alb_um.Modulos.Admn;

import java.util.Set;

import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_admin")
public class Ent_UsuarioAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_admin")
    private Long idUsuarioAdmin;

    @Column(name = "cargo_servicio", length = 30)
    private String cargoServicio;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    @JsonBackReference
    private Ent_Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Entidad_Usuario usuario;

    @OneToMany(mappedBy = "usuarioAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_Cita> citas;

    // Getters y Setters
    public Long getIdUsuarioAdmin() {
        return idUsuarioAdmin;
    }

    public void setIdUsuarioAdmin(Long idUsuarioAdmin) {
        this.idUsuarioAdmin = idUsuarioAdmin;
    }

    public String getCargoServicio() {
        return cargoServicio;
    }

    public void setCargoServicio(String cargoServicio) {
        this.cargoServicio = cargoServicio;
    }

    public Ent_Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Ent_Servicio servicio) {
        this.servicio = servicio;
    }

    public Entidad_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Entidad_Usuario usuario) {
        this.usuario = usuario;
    }

      public Set<Ent_Cita> getCitas() {
        return citas;
    }

    public void setCitas(Set<Ent_Cita> citas) {
        this.citas = citas;
    }
}
