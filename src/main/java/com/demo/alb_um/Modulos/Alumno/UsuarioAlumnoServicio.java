package com.demo.alb_um.Modulos.Alumno;

import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerRepositorio;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioAlumnoServicio {

    @Autowired
    private UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;

    public Optional<AlumnoDTO> obtenerInformacionAlumnoPorUserName(String userName) {
        Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findByUsuario_UserName(userName);

        if (alumnoOpt.isPresent()) {
            Entidad_Usuario_Alumno alumno = alumnoOpt.get();
            String nombreCompleto = alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellido();

            Optional<Entidad_ActividadFisica> actividadFisicaOpt = alumno.getAlumnoActividades().stream()
                    .findFirst() // Asumiendo que el alumno está inscrito en una sola actividad
                    .map(Ent_AlumnoActividad::getActividadFisica);

            String nombreActividadFisica = actividadFisicaOpt.map(Entidad_ActividadFisica::getNombre).orElse("No inscrito");
            String diaSemana = actividadFisicaOpt.map(Entidad_ActividadFisica::getDiaSemana).orElse("N/A");
            Time hora = actividadFisicaOpt.map(Entidad_ActividadFisica::getHora).orElse(null);
            String horario = diaSemana + " " + (hora != null ? hora.toString() : "");

            String nombreCoach = actividadFisicaOpt.flatMap(actividadFisica -> actividadFisica.getCoachActividades().stream()
                    .findFirst() // Asumiendo que cada actividad tiene un solo coach
                    .map(Ent_CoachActividad::getUsuario)
                    .map(usuario -> usuario.getNombre())
            ).orElse("Sin Coach");

            return Optional.of(new AlumnoDTO(nombreCompleto, nombreActividadFisica, nombreCoach, horario));
        }

        return Optional.empty();
    }

     

    @Autowired
    private CitaRepositorio citaRepositorio;

    public List<CitaDTO> obtenerCitasPendientes(String userName) {
        List<Ent_Cita> citas = citaRepositorio.findByUsuarioAlumno_Usuario_UserName(userName);
        return citas.stream().map(this::convertirACitaDTO).collect(Collectors.toList());
    }

    private CitaDTO convertirACitaDTO(Ent_Cita cita) {
        return new CitaDTO(
           
            cita.getHorarioServicio().getDiaSemana(),
            cita.getHorarioServicio().getHora(),
            cita.getEstadoAsistencia(),
            cita.getVerificacion(),
            cita.getAutorizadoPor()
        );
    }

    @Autowired
    private InscripcionTallerRepositorio inscripcionTallerRepositorio;

    public List<TallerDTO> obtenerTalleresPendientes(String userName) {
        List<Ent_InscripcionTaller> inscripciones = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserName(userName);
        return inscripciones.stream().map(this::convertirATallerDTO).collect(Collectors.toList());
    }
    private TallerDTO convertirATallerDTO(Ent_InscripcionTaller inscripcion) {
        return new TallerDTO(
            inscripcion.getTaller().getNombre(),
            inscripcion.getTaller().getDescripcion(),
            convertirFecha(inscripcion.getTaller().getFecha()),
            convertirHora(inscripcion.getTaller().getHora()),
            inscripcion.getEstadoAsistencia()
        );
    }

    private java.time.LocalDate convertirFecha(java.sql.Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    private java.time.LocalTime convertirHora(java.sql.Time time) {
        return time != null ? time.toLocalTime() : null;
    }
}
