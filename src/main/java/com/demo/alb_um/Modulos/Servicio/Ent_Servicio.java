package com.demo.alb_um.Modulos.Servicio;

import jakarta.persistence.*;
import java.util.Set;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "servicio")
public class Ent_Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @OneToMany(mappedBy = "servicio")
     @JsonManagedReference
    private Set<Ent_UsuarioAdmin> usuariosAdmin;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_HorarioServicio> horariosservicio;

    // Getters y Setters
    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Ent_UsuarioAdmin> getUsuariosAdmin() {
        return usuariosAdmin;
    }

    public void setUsuariosAdmin(Set<Ent_UsuarioAdmin> usuariosAdmin) {
        this.usuariosAdmin = usuariosAdmin;
    }

    public Set<Ent_HorarioServicio> getHorariosservicio() {
        return horariosservicio;
    }

    public void setHorariosservicio(Set<Ent_HorarioServicio> horariosservicio) {
        this.horariosservicio = horariosservicio;
    }
    
}
