package com.demo.alb_um.Modulos.Actividad_Fisica;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadFisicaServicio {

    @Autowired
    private ActividadFisicaRepositorio actividadFisicaRepositorio;

    public List<ActividadFisicaDTO> obtenerTodasLasActividadesFisicas() {
        return actividadFisicaRepositorio.findAll().stream()
                .map(this::convertirAActividadFisicaDTO)
                .collect(Collectors.toList());
    }

    private ActividadFisicaDTO convertirAActividadFisicaDTO(Entidad_ActividadFisica actividadFisica) {
        List<AlumnoDTO> alumnos = actividadFisica.getAlumnoActividades().stream()
                .map(Ent_AlumnoActividad::getUsuarioAlumno)
                .map(alumno -> convertirAAlumnoDTO(alumno, actividadFisica))  // Pasamos la actividad física al método
                .collect(Collectors.toList());
    
        return new ActividadFisicaDTO(
                actividadFisica.getIdActividadFisica(),
                actividadFisica.getNombre(),
                actividadFisica.getGrupo(),
                actividadFisica.getDiaSemana(),
                actividadFisica.getHora(),
                alumnos
        );
    }
    

    private AlumnoDTO convertirAAlumnoDTO(Entidad_Usuario_Alumno usuarioAlumno, Entidad_ActividadFisica actividadFisica) {
        Entidad_Usuario usuario = usuarioAlumno.getUsuario();
    
        // Construye el horario combinando diaSemana y hora
        String horario = actividadFisica.getDiaSemana() + " " + (actividadFisica.getHora() != null ? actividadFisica.getHora().toString() : "");
    
        return new AlumnoDTO(
            usuario.getNombre() + " " + usuario.getApellido(),
            usuario.getEmail(),
            actividadFisica.getNombre(),
            horario  // Asigna el valor generado de horario
        );
    }
    
    
}



