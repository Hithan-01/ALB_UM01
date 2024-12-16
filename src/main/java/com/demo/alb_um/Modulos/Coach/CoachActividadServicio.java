package com.demo.alb_um.Modulos.Coach;

import com.demo.alb_um.DTOs.*;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;
import com.demo.alb_um.Modulos.Actividad_Fisica.*;
import com.demo.alb_um.Modulos.Alumno.*;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.*;
import com.demo.alb_um.Modulos.Listas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CoachActividadServicio {

    @Autowired private CoachActividadRepositorio coachRepositorio;
    @Autowired private RepositorioAsistenciaActividadFisica asistenciaRepositorio;
    @Autowired private ActividadFisicaRepositorio actividadRepositorio;
    @Autowired @Lazy private CoachActividadServicio coachActividadServicio;
    @Autowired private Servicio_lista listaServicio;
    @Autowired private ServicioAsistenciaActividadFisica asistenciaActividadFisicaServicio;
    @Autowired private UsuarioRepositorio usuarioRepositorio;

    public Optional<CoachDTO> obtenerCoachPorUserName(String userName) {
        return coachRepositorio.findByUsuario_UserName(userName).stream()
                .findFirst()
                .map(entCoachActividad -> crearCoachDTO(entCoachActividad.getUsuario()));
    }
    
    

 // Obtener todos los usuarios con el rol "COACH"
 public List<CoachDTO> obtenerCoaches() {
        List<Entidad_Usuario> usuarios = usuarioRepositorio.findByRol("COACH");

        // Verifica si hay usuarios con el rol y conviértelos a DTO
        return usuarios.stream()
                .map(this::crearCoachDTO)
                .distinct() // Evita duplicados si los hay
                .collect(Collectors.toList());
    }

    // Método privado para transformar Entidad_Usuario a CoachDTO
    private CoachDTO crearCoachDTO(Entidad_Usuario usuario) {
        return new CoachDTO(
                usuario.getIdUsuario(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(),
                usuario.getUserName(),
                usuario.getRol() // Este campo adicional para mostrar el rol
        );
    }

    public ActividadFisicaDTO obtenerActividadPorId(Long idActividadFisica) {
        return actividadRepositorio.findById(idActividadFisica)
                .map(this::convertirAActividadFisicaDTO)
                .orElseThrow(() -> new NoSuchElementException("Actividad no encontrada"));
    }

    public Entidad_ActividadFisica obtenerActividadFisicaEntidadPorId(Long idActividadFisica) {
        return actividadRepositorio.findById(idActividadFisica)
                .orElseThrow(() -> new NoSuchElementException("Actividad no encontrada"));
    }

    public PaseListaDTO iniciarPaseLista(Long idActividadFisica) {
        ActividadFisicaDTO actividadDTO = obtenerActividadPorId(idActividadFisica);
        LocalDate fechaActual = LocalDate.now();
    
        try {
            Entidad_Lista lista = listaServicio.obtenerOCrearLista(actividadDTO, fechaActual);
            List<AlumnoDTO> alumnosActualizados = asistenciaActividadFisicaServicio.obtenerAlumnosConAsistencia(actividadDTO.getAlumnos(), lista);
    
            return new PaseListaDTO(lista, actividadDTO, alumnosActualizados);
    
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error al iniciar pase de lista: " + e.getMessage());
        }
    }

    
    private ActividadFisicaDTO convertirAActividadFisicaDTO(Entidad_ActividadFisica actividadFisica) {
        List<AlumnoDTO> alumnos = actividadFisica.getAlumnoActividades().stream()
                .map(alumnoActividad -> convertirAAlumnoDTO(alumnoActividad, actividadFisica))
                .collect(Collectors.toList());
    
        return new ActividadFisicaDTO(
                actividadFisica.getIdActividadFisica(),
                actividadFisica.getNombre(),
                actividadFisica.getGrupo(),
                actividadFisica.getDiaSemana(),
                actividadFisica.getHora(),
                actividadFisica.getIdentificadorGrupo(), // Nuevo parámetro
                alumnos
        );
    }
    

    private AlumnoDTO convertirAAlumnoDTO(Ent_AlumnoActividad alumnoActividad, Entidad_ActividadFisica actividadFisica) {
        Entidad_Usuario_Alumno usuarioAlumno = alumnoActividad.getUsuarioAlumno();
        Entidad_Usuario usuario = usuarioAlumno.getUsuario();
        String horario = actividadFisica.getDiaSemana() + " " + Optional.ofNullable(actividadFisica.getHora()).map(Object::toString).orElse("");

        Optional<Entidad_Lista> listaOpt = actividadFisica.getListas().stream().findFirst();
        Optional<Ent_AsistenciaActividadFisica> asistencia = listaOpt.flatMap(lista -> 
                asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, usuarioAlumno));

        boolean yaAsistio = asistencia.map(Ent_AsistenciaActividadFisica::isPresente).orElse(false);
        LocalDateTime fechaRegistro = asistencia.map(Ent_AsistenciaActividadFisica::getFechaRegistro).orElse(null);

        return new AlumnoDTO(
                usuarioAlumno.getIdUsuarioAlumno(),
                usuario.getNombre() + " " + usuario.getApellido(),
                actividadFisica.getNombre(),
                usuario.getEmail(),
                horario,
                yaAsistio,
                fechaRegistro,
                usuarioAlumno.getFacultad(),
                usuarioAlumno.getResidencia(),
                usuarioAlumno.getSemestre()
        );
    }
}

