package com.demo.alb_um.Modulos.Taller;

import jakarta.persistence.*;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.Duration;

import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "taller")
public class Ent_Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taller")
    private Long idTaller;

    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha")
    private java.sql.Date fecha;

    @Column(name = "hora")
    private java.sql.Time hora;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "cupos")
    private Integer cupos;

    @Column(name = "cupos_disponibles")
    private Integer cuposDisponibles;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoTaller estado = EstadoTaller.PROGRAMADO;

    @Column(name = "tiempo_transcurrido")
    private Integer tiempoTranscurrido = 0;

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ent_InscripcionTaller> inscripciones;

    public enum EstadoTaller {
        PROGRAMADO,        // Taller aún no inicia y no está en período de registro
        REGISTRO_ABIERTO,  // 10 minutos antes del inicio, se permite registro
        EN_CURSO,         // Taller en progreso
        FINALIZADO        // Taller terminado
    }

    // Constructor por defecto
    public Ent_Taller() {
        this.tiempoTranscurrido = 0;
        this.estado = EstadoTaller.PROGRAMADO;
    }

    // Constructor completo
    public Ent_Taller(String nombre, String descripcion, java.sql.Date fecha, 
                      java.sql.Time hora, Integer duracion, Integer cupos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.cupos = cupos;
        this.cuposDisponibles = cupos;
        this.tiempoTranscurrido = 0;
        this.estado = EstadoTaller.PROGRAMADO;
    }

    // Métodos auxiliares para manejo de tiempo
    @Transient
    public LocalDateTime getFechaHoraInicio() {
        return LocalDateTime.of(this.fecha.toLocalDate(), this.hora.toLocalTime());
    }

    @Transient
    public LocalDateTime getInicioRegistro() {
        return getFechaHoraInicio().minusMinutes(10);
    }

    @Transient
    public LocalDateTime getFinRegistroValido() {
        return getFechaHoraInicio().plusMinutes(10);
    }

    @Transient
    public boolean puedeRegistrarLlegada() {
        LocalDateTime ahora = LocalDateTime.now();
        return (ahora.isAfter(getInicioRegistro()) || ahora.isEqual(getInicioRegistro())) 
               && estado != EstadoTaller.FINALIZADO;
    }

    @Transient
    public boolean esLlegadaValida(LocalDateTime horaLlegada) {
        return !horaLlegada.isAfter(getFinRegistroValido());
    }

    // Método para actualizar el estado basado en el tiempo actual
    public void actualizarEstado() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioTaller = getFechaHoraInicio();
        
        if (estado != EstadoTaller.FINALIZADO) {
            if (ahora.isBefore(getInicioRegistro())) {
                this.estado = EstadoTaller.PROGRAMADO;
            } else if (ahora.isBefore(inicioTaller)) {
                this.estado = EstadoTaller.REGISTRO_ABIERTO;
            } else {
                this.estado = EstadoTaller.EN_CURSO;
                this.tiempoTranscurrido = (int) Duration.between(inicioTaller, ahora).toMinutes();
            }
        }
    }

    // Getters y Setters
    public Long getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Long idTaller) {
        this.idTaller = idTaller;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public java.sql.Time getHora() {
        return hora;
    }

    public void setHora(java.sql.Time hora) {
        this.hora = hora;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public EstadoTaller getEstado() {
        return estado;
    }

    public void setEstado(EstadoTaller estado) {
        this.estado = estado;
    }

    public Integer getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public void setTiempoTranscurrido(Integer tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }

    public Set<Ent_InscripcionTaller> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(Set<Ent_InscripcionTaller> inscripciones) {
        this.inscripciones = inscripciones;
    }

    // Métodos de utilidad
    public boolean tieneEspacioDisponible() {
        return this.cuposDisponibles > 0;
    }

    public void decrementarCuposDisponibles() {
        if (this.cuposDisponibles > 0) {
            this.cuposDisponibles--;
        }
    }

    public boolean estaEnHorarioDeRegistro() {
        LocalDateTime ahora = LocalDateTime.now();
        return ahora.isAfter(getInicioRegistro()) && ahora.isBefore(getFechaHoraInicio().plusMinutes(10));
    }

    public boolean puedeIniciar() {
        return estado == EstadoTaller.REGISTRO_ABIERTO && 
               LocalDateTime.now().isAfter(getFechaHoraInicio());
    }

    public boolean puedeFinalizar() {
        return estado == EstadoTaller.EN_CURSO;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Ent_Taller{" +
                "idTaller=" + idTaller +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                ", cuposDisponibles=" + cuposDisponibles +
                '}';
    }
}