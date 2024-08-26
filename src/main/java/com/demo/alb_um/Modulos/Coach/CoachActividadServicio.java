package com.demo.alb_um.Modulos.Coach;

import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoachActividadServicio {

    @Autowired
    private CoachActividadRepositorio coachRepositorio;

    public Optional<CoachDTO> obtenerCoachPorUserName(String userName) {
        List<Ent_CoachActividad> actividades = coachRepositorio.findByUsuario_UserName(userName);
        if (actividades.isEmpty()) {
            return Optional.empty();
        }

        Entidad_Usuario coach = actividades.get(0).getUsuario(); // Suponiendo que el coach es el mismo en todas las actividades
        List<ActividadFisicaDTO> actividadesDTO = actividades.stream()
                .map(Ent_CoachActividad::getActividadFisica)
                .map(this::convertirAActividadFisicaDTO)
                .collect(Collectors.toList());

        return Optional.of(new CoachDTO(
                coach.getNombre() + " " + coach.getApellido(),
                coach.getEmail(),
                actividadesDTO
        ));
    }

    private ActividadFisicaDTO convertirAActividadFisicaDTO(Entidad_ActividadFisica actividadFisica) {
        List<AlumnoDTO> alumnos = actividadFisica.getAlumnoActividades().stream()
                .map(Ent_AlumnoActividad::getUsuarioAlumno)
                .map(alumno -> convertirAAlumnoDTO(alumno, actividadFisica))
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
        String horario = actividadFisica.getDiaSemana() + " " + (actividadFisica.getHora() != null ? actividadFisica.getHora().toString() : "");

        return new AlumnoDTO(
            usuario.getNombre() + " " + usuario.getApellido(),
            usuario.getEmail(),
            actividadFisica.getNombre(),
            horario
        );
    }
}
