package com.demo.alb_um.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;

public class TallerDTO {
    private Long idTaller;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer duracion;  // Nuevo campo
    private Integer cuposDisponibles;
    private boolean estaInscrito;
    private boolean tallerLleno;
    private EstadoTaller estado;
    private Integer tiempoTranscurrido;
    private boolean puedeTomarLista;
    private boolean puedeRegistrarLlegada;
    private LocalDateTime fechaHoraCompleta;
    private LocalDateTime inicioRegistroLlegada;
    private LocalDateTime finRegistroLlegadaValida;
    private String lugar; // 🔹 Nuevo campo agregado


    // Constructor actualizado
    public TallerDTO(Long idTaller, String nombre, String descripcion, 
            LocalDate fecha, LocalTime hora, Integer duracion, 
            Integer cuposDisponibles, EstadoTaller estado, 
            Integer tiempoTranscurrido, String lugar) { // 🔹 Se agrega "lugar"
        this.idTaller = idTaller;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.cuposDisponibles = cuposDisponibles;
        this.estado = estado;
        this.tiempoTranscurrido = tiempoTranscurrido;
        this.puedeTomarLista = estado == EstadoTaller.FINALIZADO;
        this.lugar = lugar; // 🔹 Se inicializa el nuevo campo
        
        // Inicializar los nuevos campos
        this.fechaHoraCompleta = LocalDateTime.of(fecha, hora);
        this.inicioRegistroLlegada = fechaHoraCompleta.minusMinutes(10);
        this.finRegistroLlegadaValida = fechaHoraCompleta.plusMinutes(10);
        this.puedeRegistrarLlegada = calcularPuedeRegistrarLlegada();
    }

    // Getters y Setters
    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public boolean isEstaInscrito() {
        return estaInscrito;
    }

    public void setEstaInscrito(boolean estaInscrito) {
        this.estaInscrito = estaInscrito;
    }

    public boolean isTallerLleno() {
        return tallerLleno;
    }

    public void setTallerLleno(boolean tallerLleno) {
        this.tallerLleno = tallerLleno;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public EstadoTaller getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoTaller estado) {
        this.estado = estado;
    }
    
    // Getters y setters para tiempoTranscurrido
    public Integer getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
    
    public void setTiempoTranscurrido(Integer tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }
    
    // Getters y setters para puedeTomarLista
    public boolean isPuedeTomarLista() {
        return puedeTomarLista;
    }
    
    public void setPuedeTomarLista(boolean puedeTomarLista) {
        this.puedeTomarLista = puedeTomarLista;
    }

    private boolean calcularPuedeRegistrarLlegada() {
        LocalDateTime ahora = LocalDateTime.now();
        return (ahora.isAfter(inicioRegistroLlegada) || ahora.isEqual(inicioRegistroLlegada)) 
               && estado != EstadoTaller.FINALIZADO;
    }

    // Método para verificar si una llegada es válida
    public boolean esLlegadaValida(LocalDateTime horaLlegada) {
        return !horaLlegada.isAfter(finRegistroLlegadaValida);
    }

    // Nuevos getters y setters
    public boolean isPuedeRegistrarLlegada() {
        return puedeRegistrarLlegada;
    }

    public void setPuedeRegistrarLlegada(boolean puedeRegistrarLlegada) {
        this.puedeRegistrarLlegada = puedeRegistrarLlegada;
    }

    public LocalDateTime getFechaHoraCompleta() {
        return fechaHoraCompleta;
    }

    public void setFechaHoraCompleta(LocalDateTime fechaHoraCompleta) {
        this.fechaHoraCompleta = fechaHoraCompleta;
    }

    public LocalDateTime getInicioRegistroLlegada() {
        return inicioRegistroLlegada;
    }

    public void setInicioRegistroLlegada(LocalDateTime inicioRegistroLlegada) {
        this.inicioRegistroLlegada = inicioRegistroLlegada;
    }

    public LocalDateTime getFinRegistroLlegadaValida() {
        return finRegistroLlegadaValida;
    }

    public void setFinRegistroLlegadaValida(LocalDateTime finRegistroLlegadaValida) {
        this.finRegistroLlegadaValida = finRegistroLlegadaValida;
    }
}
