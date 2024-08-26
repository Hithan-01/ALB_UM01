package com.demo.alb_um.Modulos.Usuario;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Entidad_Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "user_name", nullable = false, length = 77)
    private String userName;

    @Column(name = "nombre", length = 30)
    private String nombre;

    @Column(name = "apellido", length = 30)
    private String apellido;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "contrasena", length = 255)
    private String contrasena;

    @Column(name = "rol", length = 20)
    private String rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Entidad_Usuario_Alumno> alumnos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_CoachActividad> coachActividades;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_UsuarioAdmin> admins;


    // Getters y setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Set<Entidad_Usuario_Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Set<Entidad_Usuario_Alumno> alumnos) {
        this.alumnos = alumnos;
    }
    public Set<Ent_CoachActividad> getCoachActividades() {
        return coachActividades;
    }

    public void setCoachActividades(Set<Ent_CoachActividad> coachActividades) {
        this.coachActividades = coachActividades;
    }

    public Set<Ent_UsuarioAdmin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Ent_UsuarioAdmin> admins) {
        this.admins = admins;
    }
}
