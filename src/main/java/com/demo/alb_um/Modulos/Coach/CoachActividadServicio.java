package com.demo.alb_um.Modulos.Coach;

import com.demo.alb_um.DTOs.*;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;
import com.demo.alb_um.Modulos.Actividad_Fisica.*;
import com.demo.alb_um.Modulos.Alumno.*;

import com.demo.alb_um.Modulos.Asitencia_Act.*;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import com.demo.alb_um.Modulos.Facultad.Entidad_facultad;
import com.demo.alb_um.Modulos.Listas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class CoachActividadServicio {

    @Autowired private CoachActividadRepositorio coachRepositorio;
 
    @Autowired private ActividadFisicaRepositorio actividadRepositorio;
    @Autowired @Lazy private CoachActividadServicio coachActividadServicio;
    @Autowired private Servicio_lista listaServicio;
    @Autowired private ServicioAsistenciaActividadFisica asistenciaActividadFisicaServicio;
    @Autowired private UsuarioRepositorio usuarioRepositorio;
    @Autowired private CoachActividadRepositorio coachActividadRepositorio;

    public Optional<CoachDTO> obtenerCoachPorUserName(String userName) {
        return coachRepositorio.findByUsuario_UserName(userName).stream()
                .findFirst()
                .map(entCoachActividad -> crearCoachDTO(entCoachActividad.getUsuario()));
    }
    

    // Obtener todos los usuarios con el rol "COACH"
    public List<CoachDTO> obtenerCoaches() {
        List<Entidad_Usuario> usuarios = usuarioRepositorio.findByRol("COACH");

        return usuarios.stream()
                .map(this::crearCoachDTO)
                .distinct() // Evita duplicados
                .collect(Collectors.toList());
    }

    public List<CoachDTO> obtenerCoachesDisponibles(Long coachActualId) {
        List<Entidad_Usuario> usuarios = usuarioRepositorio.findByRolAndIdUsuarioNot("COACH", coachActualId);
    
        return usuarios.stream()
                .map(this::crearCoachDTO)
                .collect(Collectors.toList());
    }
    
    

    // Método privado para transformar Entidad_Usuario a CoachDTO
    private CoachDTO crearCoachDTO(Entidad_Usuario usuario) {
        return new CoachDTO(
                usuario.getIdUsuario(),                               // ID del usuario
                usuario.getNombre() + " " + usuario.getApellido(),    // Nombre completo
                usuario.getEmail(),                                   // Email
                usuario.getUserName(),                                // Username (matrícula)
                usuario.getRol()                                      // Rol del usuario
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
    // Obtener los alumnos asociados a la actividad
    List<AlumnoDTO> alumnos = actividadFisica.getAlumnoActividades().stream()
            .map(alumnoActividad -> {
                Entidad_Usuario_Alumno usuarioAlumno = alumnoActividad.getUsuarioAlumno();
                Entidad_Usuario usuario = usuarioAlumno.getUsuario();
                Entidad_carrera carrera = usuarioAlumno.getCarrera(); // Obtener la carrera asociada
                Entidad_facultad facultad = carrera != null ? carrera.getFacultad() : null; // Obtener la facultad desde la carrera

                return new AlumnoDTO(
                        usuarioAlumno.getIdUsuarioAlumno(),
                        usuario.getNombre() + " " + usuario.getApellido(),
                        actividadFisica.getNombre(),
                        null, // Coach no definido en esta relación
                        actividadFisica.getHora() != null ? actividadFisica.getHora().toString() : "Sin horario",
                        false, // yaAsistio (por defecto, actualiza si es necesario)
                        null, // Fecha de registro (por defecto)
                        facultad != null ? facultad.getNombre() : "Sin Facultad",
                        usuarioAlumno.getResidencia(),
                        usuarioAlumno.getSemestre(),
                        "FALTA" // Estado por defecto (puedes ajustar la lógica si es necesario)
                );
            })
            .collect(Collectors.toList());

    // Obtener el nombre del coach asociado
    String nombreCoach = actividadFisica.getCoachActividades().stream()
            .map(coachActividad -> {
                Entidad_Usuario coachUsuario = coachActividad.getUsuario();
                return coachUsuario.getNombre() + " " + coachUsuario.getApellido();
            })
            .findFirst() // Asumimos un único coach asociado
            .orElse("Sin Coach");

    // Crear y devolver el DTO de Actividad Física
    return new ActividadFisicaDTO(
            actividadFisica.getIdActividadFisica(),
            actividadFisica.getNombre(),
            actividadFisica.getGrupo(),
            actividadFisica.getDiaSemana(),
            actividadFisica.getHora(),
            actividadFisica.getIdentificadorGrupo(),
            nombreCoach,
            alumnos
    );
}



    public List<ActividadFisicaDTO> obtenerActividadesPorCoach(Long idCoach) {
        List<Ent_CoachActividad> coachActividades = coachActividadRepositorio.findByUsuario_IdUsuario(idCoach);
    
        return coachActividades.stream()
                .map(coachActividad -> {
                    Entidad_ActividadFisica actividad = coachActividad.getActividadFisica();
                    return new ActividadFisicaDTO(
                            actividad.getIdActividadFisica(),
                            actividad.getNombre(),
                            actividad.getGrupo(),
                            actividad.getDiaSemana(),
                            actividad.getHora(),
                            actividad.getIdentificadorGrupo(),
                            "Sin Coach", // Ajusta si es necesario
                            null // Otros parámetros opcionales
                    );
                })
                .collect(Collectors.toList());
    }


}

