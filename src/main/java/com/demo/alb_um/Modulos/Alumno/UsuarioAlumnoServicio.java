package com.demo.alb_um.Modulos.Alumno;

import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;

import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;

import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerRepositorio;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioAlumnoServicio {

    @Autowired
    private UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;
  
    @Autowired
private RepositorioAsistenciaActividadFisica asistenciaRepositorio; 

public Optional<AlumnoDTO> obtenerInformacionAlumnoPorUserName(String userName) {
    // Obtener el alumno basado en su nombre de usuario
    Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findByUsuario_UserName(userName);

    if (alumnoOpt.isPresent()) {
        Entidad_Usuario_Alumno alumno = alumnoOpt.get();
        Long idUsuarioAlumno = alumno.getIdUsuarioAlumno();
        String nombreCompleto = alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellido();

        // Obtener la actividad física asociada al alumno
        Optional<Entidad_ActividadFisica> actividadFisicaOpt = alumno.getAlumnoActividades().stream()
                .findFirst()  // Si el alumno está inscrito en una sola actividad
                .map(Ent_AlumnoActividad::getActividadFisica);

        String nombreActividadFisica = actividadFisicaOpt.map(Entidad_ActividadFisica::getNombre).orElse("No inscrito");
        String diaSemana = actividadFisicaOpt.map(Entidad_ActividadFisica::getDiaSemana).orElse("N/A");
        Time hora = actividadFisicaOpt.map(Entidad_ActividadFisica::getHora).orElse(null);
        String horario = diaSemana + " " + (hora != null ? hora.toString() : "");

        // Obtener el nombre del coach, si está asignado
        String nombreCoach = actividadFisicaOpt.flatMap(actividadFisica -> actividadFisica.getCoachActividades().stream()
                .findFirst()
                .map(Ent_CoachActividad::getUsuario)
                .map(usuario -> usuario.getNombre())
        ).orElse("Sin Coach");

        // Buscar la lista más reciente de la actividad física
        Optional<Entidad_Lista> listaOpt = actividadFisicaOpt.flatMap(actividadFisica ->
                actividadFisica.getListas().stream()
                .findFirst()  // Aquí puedes ajustar la lógica si manejas varias listas
        );

        // Buscar la asistencia del alumno en la lista más reciente
        Optional<Ent_AsistenciaActividadFisica> asistenciaOpt = listaOpt.flatMap(lista ->
                asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, alumno)
        );

        // Verificar si el alumno ya asistió y obtener la fecha de registro
        boolean yaAsistio = asistenciaOpt.map(Ent_AsistenciaActividadFisica::isPresente).orElse(false);
        LocalDateTime fechaRegistro = asistenciaOpt.map(Ent_AsistenciaActividadFisica::getFechaRegistro).orElse(null);

        // Devolver el DTO con la información del alumno
        return Optional.of(new AlumnoDTO(
                idUsuarioAlumno,
                nombreCompleto,
                nombreActividadFisica,
                nombreCoach,
                horario,
                yaAsistio,
                fechaRegistro
        ));
    }

    // Si no se encuentra el alumno, devolver Optional.empty()
    return Optional.empty();
}

    
     

    @Autowired
    private CitaRepositorio citaRepositorio;

    

    private CitaDTO convertirACitaDTO(Ent_Cita cita) {
        return new CitaDTO(
            cita.getUsuarioAdmin().getServicio().getNombre(), 
            cita.getHorarioServicio().getDiaSemana(),
            cita.getHorarioServicio().getHora(),
            cita.getEstadoAsistencia(),
            cita.getVerificacion(),
            cita.getAutorizadoPor()
        );
    }

    @Autowired
    private InscripcionTallerRepositorio inscripcionTallerRepositorio;

   
    
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

    public List<CitaDTO> obtenerCitasConfirmadas(String userName) {
        List<Ent_Cita> citasConfirmadas = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName);
        return citasConfirmadas.stream().map(this::convertirACitaDTO).collect(Collectors.toList());
    }
    
    public List<TallerDTO> obtenerTalleresConfirmados(String userName) {
        List<Ent_InscripcionTaller> talleresConfirmados = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName);
        return talleresConfirmados.stream().map(this::convertirATallerDTO).collect(Collectors.toList());
    }

    public List<CitaDTO> obtenerCitasPendientes(String userName) {
        List<Ent_Cita> citasPendientes = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(userName);
        return citasPendientes.stream().map(this::convertirACitaDTO).collect(Collectors.toList());
    }
    
    public List<TallerDTO> obtenerTalleresPendientes(String userName) {
        List<Ent_InscripcionTaller> talleresPendientes = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(userName);
        return talleresPendientes.stream().map(this::convertirATallerDTO).collect(Collectors.toList());
    }
    

    public int calcularProgresoAlumno(String userName) {
        int totalAsistencias = 0;
    

        List<Ent_Cita> citasConfirmadas = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName);
        totalAsistencias += citasConfirmadas.size();

        List<Ent_InscripcionTaller> talleresConfirmados = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName);
        totalAsistencias += talleresConfirmados.size();
    
        int progreso = (int) (((double) totalAsistencias / 4) * 100);
        return Math.min(progreso, 100); 
    }
    
}
