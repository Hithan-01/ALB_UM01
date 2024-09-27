package com.demo.alb_um.Modulos.Citas;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;

import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class CitaServicio {

    @Autowired
    private CitaRepositorio citaRepositorio;

    @Autowired HorarioServicioRepositorio horarioServicioRepositorio;

   

    public List<Ent_Cita> obtenerTodasLasCitas() {
        return citaRepositorio.findAll();
    }

    public Optional<Ent_Cita> obtenerCitaPorId(Long id) {
        return citaRepositorio.findById(id);
    }

    public Ent_Cita guardarCita(Ent_Cita cita) {
        return citaRepositorio.save(cita);
    }

    public void eliminarCita(Long id) {
        citaRepositorio.deleteById(id);
    }

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
        // Obtener al alumno basado en su ID
        Entidad_Usuario_Alumno usuarioAlumno = usuarioAlumnoRepositorio.findById(idUsuarioAlumno)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        // Crear la nueva entidad de cita
        Ent_Cita nuevaCita = new Ent_Cita();
        nuevaCita.setUsuarioAlumno(usuarioAlumno);
        nuevaCita.setEstadoAsistencia(estadoAsistencia);
        nuevaCita.setVerificacion(true); // La asistencia está verificada
        nuevaCita.setAutorizadoPor(admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido());
        nuevaCita.setUsuarioAdmin(admin);

        // Si es un servicio como Antropometría, obtener el horario servicio.
        if (admin.getServicio().getNombre().equalsIgnoreCase("Antropometria")) {
            Ent_HorarioServicio horarioServicio = obtenerHorarioServicioParaCita(admin.getServicio().getIdServicio()); 
            nuevaCita.setHorarioServicio(horarioServicio);
        } else {
            // En servicios como limpieza dental o fisioterapia, el horario no se fija.
            nuevaCita.setHorarioServicio(null);
        }

        // Guardar la nueva cita en la base de datos
        citasRepositorio.save(nuevaCita);
    }

   


}
    

