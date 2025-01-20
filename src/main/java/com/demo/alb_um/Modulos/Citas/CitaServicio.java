package com.demo.alb_um.Modulos.Citas;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;

import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioRepositorio;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class CitaServicio {

    

    @Autowired HorarioServicioRepositorio horarioServicioRepositorio;


    public Ent_HorarioServicio obtenerHorarioServicioParaCita(Long servicioId) {
        // Lógica para obtener el horario según el servicio
        return horarioServicioRepositorio.findFirstByServicio_IdServicio(servicioId)
                .orElseThrow(() -> new IllegalArgumentException("No hay horarios disponibles para este servicio."));
    }
    
    @Autowired
    private CitaRepositorio citasRepositorio;

    @Autowired
    private UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;

public void generarCita(Ent_UsuarioAdmin admin, Long idUsuarioAlumno, String estadoAsistencia) {
    // Buscar al alumno
    Entidad_Usuario_Alumno usuarioAlumno = usuarioAlumnoRepositorio.findById(idUsuarioAlumno)
            .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

    // Obtener el servicio asociado al administrador
    Ent_Servicio servicioAdmin = admin.getServicio();
    if (servicioAdmin == null) {
        throw new IllegalArgumentException("El administrador no tiene un servicio asociado.");
    }

    // Crear un nuevo horario dinámico con la fecha y hora actuales
    Ent_HorarioServicio nuevoHorario = new Ent_HorarioServicio();
    nuevoHorario.setDiaSemana(LocalDate.now()); // Fecha actual
    nuevoHorario.setHora(LocalTime.now()); // Hora actual
    nuevoHorario.setDisponible(false); // No disponible para otras citas
    nuevoHorario.setServicio(servicioAdmin); // Asignar el servicio del administrador

    // Guardar el horario en la base de datos
    nuevoHorario = horarioServicioRepositorio.save(nuevoHorario);

    // Crear la cita asociada al alumno y al horario dinámico
    Ent_Cita nuevaCita = new Ent_Cita();
    nuevaCita.setUsuarioAlumno(usuarioAlumno);
    nuevaCita.setUsuarioAdmin(admin);
    nuevaCita.setAutorizadoPor(admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido());
    nuevaCita.setVerificacion(true);
    nuevaCita.setEstadoAsistencia(estadoAsistencia);
    nuevaCita.setHorarioServicio(nuevoHorario); // Asociar el horario dinámico

    // Guardar la cita en la base de datos
    citasRepositorio.save(nuevaCita);
}


    
    public void generarCitaParaAntropometria(Long idUsuarioAlumno, Long horarioId, Ent_UsuarioAdmin admin) {
        // Obtener al alumno y el horario basados en sus respectivos IDs
        Entidad_Usuario_Alumno usuarioAlumno = usuarioAlumnoRepositorio.findById(idUsuarioAlumno)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + idUsuarioAlumno));
    
        Ent_HorarioServicio horarioServicio = horarioServicioRepositorio.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado para Antropometría con ID: " + horarioId));
    
        // Crear una nueva cita
        Ent_Cita nuevaCita = new Ent_Cita();
        nuevaCita.setUsuarioAlumno(usuarioAlumno);
        nuevaCita.setHorarioServicio(horarioServicio);
        nuevaCita.setVerificacion(false);  // Para Antropometría, la verificación es pendiente
        nuevaCita.setEstadoAsistencia("pendiente");  // Estado predeterminado
        nuevaCita.setUsuarioAdmin(admin);  // Asignar el administrador responsable
        nuevaCita.setAutorizadoPor(admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido());  // Autorización del admin
    
        // Marcar el horario como no disponible
        horarioServicio.setDisponible(false);
    
        // Guardar la nueva cita y actualizar el estado del horario en una sola transacción
        citasRepositorio.save(nuevaCita);
        horarioServicioRepositorio.save(horarioServicio);
    }
    
    /* 
    public void validarCita(Long citaId, Ent_UsuarioAdmin admin) {
        // Buscar la cita por su ID
        Ent_Cita cita = citasRepositorio.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        // Actualizar el estado de la cita
        cita.setVerificacion(true); // Se marca como validada
        cita.setEstadoAsistencia("asistió"); // Cambiar el estado de asistencia a 'asistió'
        cita.setUsuarioAdmin(admin); // Registrar el admin que valida la cita
        cita.setAutorizadoPor(admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido()); // Registrar el admin que autorizó

        // Guardar la cita actualizada en la base de datos
        citasRepositorio.save(cita);
    }
    */
    
    public boolean tieneCitaParaAntropometria(Long idAlumno) {
        // Buscar si ya existe una cita para el alumno en el servicio de Antropometría
        return citasRepositorio.existsByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_Nombre(idAlumno, "Antropometria");
    }
    
    public boolean alumnoTieneCitaConfirmada(Long alumnoId, Long servicioId) {
        return citasRepositorio.existsByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_IdServicioAndVerificacionTrue(alumnoId, servicioId);
    }
    

    
}