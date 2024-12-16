package com.demo.alb_um.Modulos.Alumno;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerInscripcionDTO;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerRepositorio;
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

    @Autowired
    private CitaRepositorio citaRepositorio;

    @Autowired
    private InscripcionTallerRepositorio inscripcionTallerRepositorio;



    // Obtener información completa del alumno por nombre de usuario
    public Optional<AlumnoDTO> obtenerInformacionAlumnoPorUserName(String userName) {
        return usuarioAlumnoRepositorio.findByUsuario_UserName(userName)
                .map(this::convertirAAlumnoDTOConActividad);
        }

        // Convertir una entidad de alumno en un DTO con la actividad física asociada y la asistencia
        private AlumnoDTO convertirAAlumnoDTOConActividad(Entidad_Usuario_Alumno alumno) {
        Long idUsuarioAlumno = alumno.getIdUsuarioAlumno();
        String nombreCompleto = alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellido();
        String facultad = alumno.getFacultad();
        String residencia = alumno.getResidencia();
        String semestre = alumno.getSemestre();

        Optional<Entidad_ActividadFisica> actividadOpt = alumno.getAlumnoActividades().stream()
                .findFirst()
                .map(Ent_AlumnoActividad::getActividadFisica);

        String nombreActividad = actividadOpt.map(Entidad_ActividadFisica::getNombre).orElse("No inscrito");
        String horario = actividadOpt.map(this::obtenerHorarioDeActividad).orElse("N/A");
        String nombreCoach = obtenerNombreCoach(actividadOpt);

        boolean yaAsistio = obtenerAsistenciaAlumno(actividadOpt, alumno);
        LocalDateTime fechaRegistro = obtenerFechaRegistroAsistencia(actividadOpt, alumno);

        return new AlumnoDTO(idUsuarioAlumno, nombreCompleto, nombreActividad, nombreCoach, horario,
                yaAsistio, fechaRegistro, facultad, residencia, semestre);
    }

    // Obtener el horario de la actividad física
    private String obtenerHorarioDeActividad(Entidad_ActividadFisica actividad) {
        String diaSemana = actividad.getDiaSemana();
        Time hora = actividad.getHora();
        return diaSemana + " " + (hora != null ? hora.toString() : "");
    }

    // Obtener el nombre del coach
    private String obtenerNombreCoach(Optional<Entidad_ActividadFisica> actividadOpt) {
        return actividadOpt.flatMap(actividad -> actividad.getCoachActividades().stream()
                .findFirst()
                .map(coachActividad -> coachActividad.getUsuario().getNombre()))
                .orElse("Sin Coach");
    }

    // Obtener la asistencia del alumno a la actividad más reciente
    private boolean obtenerAsistenciaAlumno(Optional<Entidad_ActividadFisica> actividadOpt, Entidad_Usuario_Alumno alumno) {
        return actividadOpt.flatMap(actividad -> actividad.getListas().stream().findFirst())
                .flatMap(lista -> asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, alumno))
                .map(Ent_AsistenciaActividadFisica::isPresente)
                .orElse(false);
    }

    // Obtener la fecha de registro de asistencia del alumno
    private LocalDateTime obtenerFechaRegistroAsistencia(Optional<Entidad_ActividadFisica> actividadOpt, Entidad_Usuario_Alumno alumno) {
        return actividadOpt.flatMap(actividad -> actividad.getListas().stream().findFirst())
                .flatMap(lista -> asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, alumno))
                .map(Ent_AsistenciaActividadFisica::getFechaRegistro)
                .orElse(null);
    }

    // Obtener las citas confirmadas
    public List<CitaDTO> obtenerCitasConfirmadas(String userName) {
        return citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName)
                .stream().map(this::convertirACitaDTO).collect(Collectors.toList());
    }

    // Obtener los talleres confirmados
    public List<TallerInscripcionDTO> obtenerTalleresConfirmados(String userName) {
        return inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName)
                .stream().map(this::convertirATallerDTO).collect(Collectors.toList());
    }

    public List<CitaDTO> obtenerCitasPendientes(String userName) {
        List<Ent_Cita> citasPendientes = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(userName);
        return citasPendientes.stream().map(this::convertirACitaDTO).collect(Collectors.toList());
    }

    // Obtener los talleres pendientes
    public List<TallerInscripcionDTO> obtenerTalleresPendientes(String userName) {
        List<Ent_InscripcionTaller> talleresPendientes = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(userName);
        return talleresPendientes.stream().map(this::convertirATallerDTO).collect(Collectors.toList());
    }


    // Métodos de conversión
    private CitaDTO convertirACitaDTO(Ent_Cita cita) {
        return new CitaDTO(
                cita.getIdCita(),
                cita.getUsuarioAdmin().getServicio().getNombre(),
                cita.getHorarioServicio() != null ? cita.getHorarioServicio().getDiaSemana() : null,
                cita.getHorarioServicio() != null ? cita.getHorarioServicio().getHora() : null,
                cita.getEstadoAsistencia(),
                cita.getVerificacion(),
                cita.getAutorizadoPor(),
                cita.getUsuarioAlumno().getUsuario().getNombre() + " " + cita.getUsuarioAlumno().getUsuario().getApellido()
        );
    }

    private TallerInscripcionDTO convertirATallerDTO(Ent_InscripcionTaller inscripcion) {
        return new TallerInscripcionDTO(
                inscripcion.getTaller().getNombre(),
                inscripcion.getTaller().getDescripcion(),
                inscripcion.getTaller().getFecha(),
                inscripcion.getTaller().getHora(),
                inscripcion.getEstadoAsistencia()
        );
    }

    // Calcular el progreso del alumno basado en las asistencias confirmadas
    public int calcularProgresoAlumno(String userName) {
        int totalCitas = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName).size();
        int totalTalleres = inscripcionTallerRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(userName).size();
        int totalAsistencias = totalCitas + totalTalleres;

        return Math.min((totalAsistencias * 100) / 4, 100); // Progreso limitado al 100%
    }

    // Buscar alumno por username o ID
    public Optional<AlumnoDTO> buscarAlumnoPorUsername(String search) {
        return usuarioAlumnoRepositorio.findByUsuario_UserName(search)
                .map(this::convertirAAlumnoDTOSimple);
    }

    public Optional<AlumnoDTO> buscarAlumnoPorId(Long alumnoId) {
        return usuarioAlumnoRepositorio.findById(alumnoId)
                .map(this::convertirAAlumnoDTOSimple);
    }

    // Conversión simplificada de Alumno
    private AlumnoDTO convertirAAlumnoDTOSimple(Entidad_Usuario_Alumno alumno) {
        return new AlumnoDTO(
                alumno.getIdUsuarioAlumno(),
                alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellido(),
                null, null, null, false, null,
                alumno.getFacultad(),
                alumno.getResidencia(),
                alumno.getSemestre()
        );
    }

    
    public Optional<Ent_Cita> obtenerCitaConfirmadaDeAntropometria(Long idUsuarioAlumno) {
        List<Ent_Cita> citas = citaRepositorio.findByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_NombreAndEstadoAsistenciaAndVerificacionTrue(
            idUsuarioAlumno, "Antropometría", "ASISTIO");
    
        // Si hay citas, devolver la más reciente (puedes ajustar esto si necesitas algo diferente)
        if (!citas.isEmpty()) {
            return Optional.of(citas.get(0));
        }
        return Optional.empty();
    }
    
}
