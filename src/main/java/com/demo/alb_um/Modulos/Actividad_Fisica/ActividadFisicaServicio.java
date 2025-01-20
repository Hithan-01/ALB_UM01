package com.demo.alb_um.Modulos.Actividad_Fisica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.RegistrarActividadDTO;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno_Actividad.AlumnoActividadId;
import com.demo.alb_um.Modulos.Alumno_Actividad.AlumnoActividadRepositorio;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import com.demo.alb_um.Modulos.Coach.CoachActividadId;
import com.demo.alb_um.Modulos.Coach.CoachActividadRepositorio;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.demo.alb_um.Modulos.Facultad.Entidad_facultad;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ActividadFisicaServicio {

    @Autowired
    private ActividadFisicaRepositorio actividadFisicaRepositorio;


    @Autowired
    private AlumnoActividadRepositorio alumnoActividadRepositorio;

    @Autowired 
    private CoachActividadRepositorio coachActividadRepositorio;

    @Autowired UsuarioRepositorio usuarioRepositorio;
    // Mapa de nombres a códigos (para identificador)
    private static final Map<String, String> CODIGO_NOMBRE = new HashMap<>();

    static {
        CODIGO_NOMBRE.put("Volleyball", "V1");
        CODIGO_NOMBRE.put("Basketball", "B2");
        CODIGO_NOMBRE.put("Futbol", "F3");
        
    }

        // Método para obtener actividad física por ID
        public Entidad_ActividadFisica obtenerPorId(Long id) {
            return actividadFisicaRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con el ID: " + id));
        }

        public List<Entidad_ActividadFisica> obtenerTodas() {
            return actividadFisicaRepositorio.findAll();
        }

        public Entidad_ActividadFisica registrarActividad(RegistrarActividadDTO dto) {
            // Convertir hora de String a Time
            Time horaConvertida = Time.valueOf(dto.getHora() + ":00"); // Agregar ":00" si falta
        
            // Generar identificador de grupo
            String identificadorGrupo = generarIdentificadorGrupo(dto.getDiaSemana(), horaConvertida, dto.getNombre());
        
            // Crear la entidad
            Entidad_ActividadFisica actividad = new Entidad_ActividadFisica();
            actividad.setNombre(dto.getNombre());
            actividad.setDiaSemana(dto.getDiaSemana());
            actividad.setHora(horaConvertida); // Usar la hora convertida
            actividad.setGrupo(dto.getGrupo());
            actividad.setIdentificadorGrupo(identificadorGrupo);
        
            // Guardar en la base de datos
            return actividadFisicaRepositorio.save(actividad);
        }
        

    // Generar el identificador de grupo
    private String generarIdentificadorGrupo(String diaSemana, Time hora, String nombre) {
        // Determina la letra del día
        String letraDia = "Lunes-Miércoles".equalsIgnoreCase(diaSemana) ? "A" : "B";
        
        // Formatea la hora como dos dígitos (ej. 07, 08)
        String horaFormato = String.format("%02d", hora.toLocalTime().getHour());
        
        // Obtiene el código de la actividad
        String codigoActividad = CODIGO_NOMBRE.getOrDefault(nombre, "XX");
    
        // Genera el identificador concatenando los valores
        return letraDia + horaFormato + codigoActividad;
    }


    @Transactional
    public void eliminarActividadFisica(Long idActividadFisica) {
        if (actividadFisicaRepositorio.existsById(idActividadFisica)) {
            // Eliminar las relaciones en coach_actividad
            coachActividadRepositorio.deleteByActividadFisica_Id(idActividadFisica);
    
            // Eliminar la actividad física
            actividadFisicaRepositorio.deleteById(idActividadFisica);
        } else {
            throw new IllegalArgumentException("La actividad con el ID " + idActividadFisica + " no existe.");
        }
    }

    public Entidad_ActividadFisica editarActividad(Long id, RegistrarActividadDTO dto) {
        // Buscar la actividad existente
        Entidad_ActividadFisica actividad = actividadFisicaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con el ID: " + id));
    
        // Convertir hora de String a Time
        Time horaConvertida = Time.valueOf(dto.getHora() + ":00"); // Agregar ":00" si falta
    
        // Generar el nuevo identificador de grupo basado en los valores actualizados
        String nuevoIdentificador = generarIdentificadorGrupo(dto.getDiaSemana(), horaConvertida, dto.getNombre());
    
        // Actualizar los campos
        actividad.setNombre(dto.getNombre());
        actividad.setDiaSemana(dto.getDiaSemana());
        actividad.setHora(horaConvertida);
        actividad.setGrupo(dto.getGrupo());
        actividad.setIdentificadorGrupo(nuevoIdentificador); // Asignar el nuevo identificador
    
        // Guardar en la base de datos
        return actividadFisicaRepositorio.save(actividad);
    }
    

    

    public List<ActividadFisicaDTO> obtenerTodasComoDTO() {
        List<Entidad_ActividadFisica> actividades = actividadFisicaRepositorio.findAll();
    
        List<ActividadFisicaDTO> resultado = actividades.stream().map(actividad -> {
            // Lógica de transformación
            String nombreCoach = actividad.getCoachActividades().stream()
                    .findFirst()
                    .map(coachActividad -> {
                        Entidad_Usuario coach = coachActividad.getUsuario();
                        return coach.getNombre() + " " + coach.getApellido();
                    })
                    .orElse(null);

    
            List<AlumnoDTO> alumnos = actividad.getAlumnoActividades().stream()
                    .map(relacion -> {
                        Entidad_Usuario_Alumno usuarioAlumno = relacion.getUsuarioAlumno();
                        Entidad_carrera carrera = usuarioAlumno.getCarrera();
                        Entidad_facultad facultad = carrera != null ? carrera.getFacultad() : null;
                        String nombreFacultad = facultad != null ? facultad.getNombre() : "Sin Facultad";
                        return new AlumnoDTO(
                            usuarioAlumno.getIdUsuarioAlumno(),
                            usuarioAlumno.getUsuario().getNombre() + " " + usuarioAlumno.getUsuario().getApellido(),
                            null, // Actividad física no aplicable aquí
                            null, // Nombre del coach no aplicable aquí
                            null, // Horario no aplicable aquí
                            false, // Ya asistió (por defecto)
                            null, // Fecha de registro (por defecto)
                            nombreFacultad, // Facultad obtenida desde la relación
                            usuarioAlumno.getResidencia(),
                            usuarioAlumno.getSemestre(),
                            "FALTA" // estadoFalta, por defecto si no tienes lógica para determinarlo aquí
                    );
                    
                    })
                    .collect(Collectors.toList());
    
            return new ActividadFisicaDTO(
                    actividad.getIdActividadFisica(),
                    actividad.getNombre(),
                    actividad.getGrupo(),
                    actividad.getDiaSemana(),
                    actividad.getHora(),
                    actividad.getIdentificadorGrupo(),
                    nombreCoach,
                    alumnos
            );
        }).collect(Collectors.toList());
    
        // Log para depuración
        resultado.forEach(dto -> {
            System.out.println("ActividadDTO: id=" + dto.getIdActividadFisica() + ", nombre=" + dto.getNombre());
        });
    
        return resultado;
    }
    

public List<AlumnoDTO> obtenerAlumnosPorActividad(Long idActividadFisica) {
    Entidad_ActividadFisica actividad = actividadFisicaRepositorio.findById(idActividadFisica)
            .orElseThrow(() -> new IllegalArgumentException("La actividad no existe."));

    return actividad.getAlumnoActividades().stream()
            .map(relacion -> {
                Entidad_Usuario_Alumno usuarioAlumno = relacion.getUsuarioAlumno();
                Entidad_carrera carrera = usuarioAlumno.getCarrera();
                Entidad_facultad facultad = carrera != null ? carrera.getFacultad() : null;
                String nombreFacultad = facultad != null ? facultad.getNombre() : "Sin Facultad";

                return new AlumnoDTO(
                    usuarioAlumno.getIdUsuarioAlumno(),
                    usuarioAlumno.getUsuario().getNombre() + " " + usuarioAlumno.getUsuario().getApellido(),
                    null, // Actividad física no aplicable aquí
                    null, // Nombre del coach no aplicable aquí
                    null, // Horario no aplicable aquí
                    false, // Ya asistió (por defecto)
                    null, // Fecha de registro (por defecto)
                    nombreFacultad, // Facultad obtenida desde la relación
                    usuarioAlumno.getResidencia(),
                    usuarioAlumno.getSemestre(),
                    "FALTA" // estadoFalta, por defecto si no tienes lógica para determinarlo aquí
            );
            
            })
            .collect(Collectors.toList());
}

public AlumnoDTO obtenerAlumnoPorId(Long idAlumno) {
    List<Ent_AlumnoActividad> alumnoActividades = 
        alumnoActividadRepositorio.findByIdIdUsuarioAlumno(idAlumno);

if (alumnoActividades.isEmpty()) {
    throw new IllegalArgumentException("No se encontró el alumno con ID: " + idAlumno);
}

Ent_AlumnoActividad alumnoActividad = alumnoActividades.get(0); // Tomar el primer elemento


    // Convertir la entidad a DTO
    Entidad_Usuario_Alumno usuarioAlumno = alumnoActividad.getUsuarioAlumno();
    Entidad_carrera carrera = usuarioAlumno.getCarrera();
    Entidad_facultad facultad = carrera != null ? carrera.getFacultad() : null;
    String nombreFacultad = facultad != null ? facultad.getNombre() : "Sin Facultad";

                return new AlumnoDTO(
                    usuarioAlumno.getIdUsuarioAlumno(),
                    usuarioAlumno.getUsuario().getNombre() + " " + usuarioAlumno.getUsuario().getApellido(),
                    null, // Actividad física no aplicable aquí
                    null, // Nombre del coach no aplicable aquí
                    null, // Horario no aplicable aquí
                    false, // Ya asistió (por defecto)
                    null, // Fecha de registro (por defecto)
                    nombreFacultad, // Facultad obtenida desde la relación
                    usuarioAlumno.getResidencia(),
                    usuarioAlumno.getSemestre(),
                    "FALTA" // estadoFalta, por defecto si no tienes lógica para determinarlo aquí
            );

}

@Transactional
public void moverAlumnoAActividad(Long idAlumno, Long idActividad) {
    Logger log = LoggerFactory.getLogger(this.getClass());
    log.info("Iniciando el proceso para mover al alumno con ID {} a la actividad con ID {}", idAlumno, idActividad);

    // Buscar la relación existente
    Ent_AlumnoActividad relacionExistente = alumnoActividadRepositorio.findByIdIdUsuarioAlumno(idAlumno).stream()
        .findFirst()
        .orElseThrow(() -> {
            log.error("El alumno con ID {} no está inscrito en ninguna actividad.", idAlumno);
            return new IllegalArgumentException("El alumno no está inscrito en ninguna actividad.");
        });

    // Buscar la nueva actividad
    Entidad_ActividadFisica nuevaActividad = actividadFisicaRepositorio.findById(idActividad)
        .orElseThrow(() -> {
            log.error("La actividad física con ID {} no existe.", idActividad);
            return new IllegalArgumentException("La actividad física no existe.");
        });

    // Crear una nueva entidad AlumnoActividad en lugar de modificar la existente
    Ent_AlumnoActividad nuevaRelacion = new Ent_AlumnoActividad();
    AlumnoActividadId nuevoId = new AlumnoActividadId(idAlumno, idActividad);
    
    nuevaRelacion.setId(nuevoId);
    nuevaRelacion.setActividadFisica(nuevaActividad);
    nuevaRelacion.setUsuarioAlumno(relacionExistente.getUsuarioAlumno());

    // Eliminar la relación antigua y guardar la nueva
    alumnoActividadRepositorio.delete(relacionExistente);
    alumnoActividadRepositorio.save(nuevaRelacion);
    
    log.info("Proceso completado: Alumno {} movido a la actividad con ID {}", idAlumno, idActividad);
}

@Transactional
public void asignarCoach(Long idActividad, Long coachId) {
    Entidad_ActividadFisica actividad = actividadFisicaRepositorio.findById(idActividad)
            .orElseThrow(() -> new IllegalArgumentException("La actividad no existe"));

    Entidad_Usuario coach = usuarioRepositorio.findById(coachId)
            .orElseThrow(() -> new IllegalArgumentException("El Coach no existe"));

    // Crear la relación Ent_CoachActividad
    Ent_CoachActividad coachActividad = new Ent_CoachActividad();
    coachActividad.setActividadFisica(actividad);
    coachActividad.setUsuario(coach);

    CoachActividadId id = new CoachActividadId(coachId, idActividad);
    coachActividad.setId(id);

    // Guardar en la tabla de relación
    coachActividadRepositorio.save(coachActividad);
}

@Transactional
public void cambiarCoach(Long idActividad, Long nuevoCoachId) {
    // Buscar la actividad
    Entidad_ActividadFisica actividad = actividadFisicaRepositorio.findById(idActividad)
            .orElseThrow(() -> new IllegalArgumentException("La actividad no existe"));

    // Eliminar la relación actual
    coachActividadRepositorio.deleteByActividadFisicaId(idActividad);

    // Crear la nueva relación
    Entidad_Usuario nuevoCoach = usuarioRepositorio.findById(nuevoCoachId)
            .orElseThrow(() -> new IllegalArgumentException("El nuevo Coach no existe"));

    Ent_CoachActividad nuevaRelacion = new Ent_CoachActividad();
    nuevaRelacion.setActividadFisica(actividad);
    nuevaRelacion.setUsuario(nuevoCoach);

    CoachActividadId nuevaRelacionId = new CoachActividadId(nuevoCoachId, idActividad);
    nuevaRelacion.setId(nuevaRelacionId);

    coachActividadRepositorio.save(nuevaRelacion);
}

// Método para convertir una actividad a DTO
public ActividadFisicaDTO convertirAActividadFisicaDTO(Entidad_ActividadFisica actividad) {
    // Lógica para obtener el nombre del Coach
    String nombreCoach = actividad.getCoachActividades().stream()
            .findFirst()
            .map(coachActividad -> {
                Entidad_Usuario coach = coachActividad.getUsuario();
                return coach.getNombre() + " " + coach.getApellido();
            })
            .orElse(null);

    // Crear el DTO
    return new ActividadFisicaDTO(
            actividad.getIdActividadFisica(),
            actividad.getNombre(),
            actividad.getGrupo(),
            actividad.getDiaSemana(),
            actividad.getHora(),
            actividad.getIdentificadorGrupo(),
            nombreCoach,
            null // No necesitas alumnos para editar
    );
}

public List<ActividadFisicaDTO> buscarPorFiltro(String filtro) {
    return actividadFisicaRepositorio.buscarPorFiltro(filtro).stream()
        .map(this::convertirAActividadFisicaDTO)
        .collect(Collectors.toList());
}


}
