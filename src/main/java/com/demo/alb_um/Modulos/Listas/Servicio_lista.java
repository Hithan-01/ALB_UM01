package com.demo.alb_um.Modulos.Listas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica.EstadoFalta;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;


@Service
public class Servicio_lista {

    private final Repositorio_lista repositorioLista;
    private final RepositorioAsistenciaActividadFisica asistenciaRepositorio;

    @Autowired
    public Servicio_lista(Repositorio_lista repositorioLista, RepositorioAsistenciaActividadFisica asistenciaRepositorio) {
        this.repositorioLista = repositorioLista;
        this.asistenciaRepositorio = asistenciaRepositorio;
    }

    public Entidad_Lista obtenerOCrearLista(ActividadFisicaDTO actividadDTO, LocalDate fechaActual) {
        Date fecha = Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Entidad_ActividadFisica actividadFisica = convertirADesdeDTO(actividadDTO);
    
        // Obtener identificador y hora programada
        String identificadorGrupo = actividadFisica.getIdentificadorGrupo();
        LocalTime horaProgramada = actividadFisica.getHora().toLocalTime();
    
        // Obtener el día actual
        String diaActual = fechaActual.getDayOfWeek().toString().toUpperCase(); // MONDAY, TUESDAY, etc.
    
        // 📌 Si la actividad es "Caminata", solo validamos el día
        if ("Caminata".equalsIgnoreCase(actividadFisica.getNombre())) {
            if (!esDiaCorrecto(identificadorGrupo, diaActual)) {
                throw new IllegalArgumentException("Hoy no es un día permitido para registrar pasos.");
            }
        } else {
            // 📌 Para otras actividades, validamos día y hora
            if (!esDiaCorrecto(identificadorGrupo, diaActual, horaProgramada)) {
                throw new IllegalArgumentException("No es el día o la hora correspondiente para iniciar la lista.");
            }
    
            // Validar si la hora actual está dentro del rango permitido
            LocalTime horaActual = LocalTime.now();
            LocalTime horaInicio = horaProgramada.minusMinutes(10); // 10 minutos antes
            LocalTime horaCierre = horaProgramada.plusMinutes(40);  // 40 minutos después
    
            if (horaActual.isBefore(horaInicio)) {
                throw new IllegalArgumentException("Espera a la hora indicada de la clase para iniciar el pase de lista.");
            }
    
            if (horaActual.isAfter(horaCierre)) {
                throw new IllegalArgumentException("El tiempo para iniciar la lista ha expirado.");
            }
        }
    
        // Si todas las validaciones son correctas, obtener o crear lista
        return repositorioLista.findByActividadFisicaAndFecha(actividadFisica, fecha)
                .orElseGet(() -> {
                    System.out.println("Creando nueva lista para la actividad.");
                    Entidad_Lista nuevaLista = new Entidad_Lista();
                    nuevaLista.setFecha(fecha);
                    nuevaLista.setActividadFisica(actividadFisica);
                    return repositorioLista.save(nuevaLista);
                });
    }
    

    private boolean esDiaCorrecto(String identificadorGrupo, String diaActual, LocalTime horaProgramada) {
        if (identificadorGrupo == null || horaProgramada == null) {
            System.out.println("El identificador o la hora programada son nulos.");
            return false;
        }
    
        String letraDia = identificadorGrupo.substring(0, 1); // A o B
        String horaIdentificador = identificadorGrupo.substring(1, 3); // 07 o 08
    
        boolean diaValido = false;
        if (letraDia.equalsIgnoreCase("A")) {
            diaValido = diaActual.equals("MONDAY") || diaActual.equals("WEDNESDAY");
        } else if (letraDia.equalsIgnoreCase("B")) {
            diaValido = diaActual.equals("TUESDAY") || diaActual.equals("THURSDAY");
        }
    
        boolean horaValida = horaProgramada.getHour() == Integer.parseInt(horaIdentificador);
    
        System.out.println("Validando día y hora:");
        System.out.println("Día actual: " + diaActual);
        System.out.println("Identificador Grupo: " + identificadorGrupo);
        System.out.println("Día válido: " + diaValido);
        System.out.println("Hora válida: " + horaValida);
    
        return diaValido && horaValida;
    }

    public boolean esDiaCorrecto(String identificadorGrupo, String diaActual) {
        if (identificadorGrupo == null) {
            System.out.println("El identificador es nulo.");
            return false;
        }
    
        String letraDia = identificadorGrupo.substring(0, 1); // A o B
        boolean diaValido = false;
    
        if (letraDia.equalsIgnoreCase("A")) {
            diaValido = diaActual.equals("MONDAY") || diaActual.equals("WEDNESDAY");
        } else if (letraDia.equalsIgnoreCase("B")) {
            diaValido = diaActual.equals("TUESDAY") || diaActual.equals("THURSDAY");
        }
    
        System.out.println("Validando día para Caminata:");
        System.out.println("Día actual: " + diaActual);
        System.out.println("Identificador Grupo: " + identificadorGrupo);
        System.out.println("Día válido: " + diaValido);
    
        return diaValido;
    }
    
    


    
private Entidad_ActividadFisica convertirADesdeDTO(ActividadFisicaDTO actividadDTO) {
    Entidad_ActividadFisica entidad = new Entidad_ActividadFisica();
    entidad.setIdActividadFisica(actividadDTO.getIdActividadFisica());
    entidad.setNombre(actividadDTO.getNombre());
    entidad.setGrupo(actividadDTO.getGrupo());
    entidad.setDiaSemana(actividadDTO.getDiaSemana());
    entidad.setHora(actividadDTO.getHora());
    entidad.setIdentificadorGrupo(actividadDTO.getIdentificadorGrupo()); // Ahora sí se asigna
    return entidad;
}

@Transactional
public synchronized void guardarAsistencia(Entidad_Lista lista, Map<String, String> asistencias, LocalDateTime horaActual) {
    Set<Ent_AlumnoActividad> alumnosActividad = lista.getActividadFisica().getAlumnoActividades();

    // Convertir la fecha de la lista
    LocalDate fechaLista = lista.getFecha().toInstant()
                                 .atZone(ZoneId.systemDefault())
                                 .toLocalDate();
    LocalTime horaInicioActividad = lista.getActividadFisica().getHora().toLocalTime();
    int asistenciaLimiteMinutos = 11; // Límite de minutos para marcar asistencia
    LocalDateTime horaLimite = LocalDateTime.of(fechaLista, horaInicioActividad).plusMinutes(asistenciaLimiteMinutos);

    // Mapear asistencias existentes de la lista
    Map<Long, Ent_AsistenciaActividadFisica> asistenciaMap = asistenciaRepositorio.findByLista(lista).stream()
            .collect(Collectors.toMap(
                    asistencia -> asistencia.getUsuarioAlumno().getIdUsuarioAlumno(),
                    asistencia -> asistencia
            ));

    for (Ent_AlumnoActividad alumnoActividad : alumnosActividad) {
        Long alumnoId = alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno();
        String estadoAsistencia = asistencias.get("asistencia-" + alumnoId);

        Ent_AsistenciaActividadFisica asistencia = asistenciaMap.getOrDefault(alumnoId, new Ent_AsistenciaActividadFisica());

        if (asistencia.getEstadoFalta() == EstadoFalta.PRESENTE) {
            continue; // No permitir cambiar asistencia ya marcada como presente
        }

        asistencia.setLista(lista);
        asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());

        if ("presente".equals(estadoAsistencia)) {
            if (horaActual.isAfter(horaLimite)) {
                // Llegó tarde, marcar como falta con hora actual
                asistencia.setEstadoFalta(EstadoFalta.FALTA);
                asistencia.setFechaRegistro(horaActual); // Registrar la hora actual para que se vea cuándo llegó
            } else {
                // Llegó a tiempo, marcar como presente
                asistencia.setEstadoFalta(EstadoFalta.PRESENTE);
                asistencia.setFechaRegistro(horaActual); // Registrar la hora real
            }
        } else if ("falta".equals(estadoAsistencia)) {
            // Marcar como falta explícitamente, usar fecha de la lista
            asistencia.setEstadoFalta(EstadoFalta.FALTA);
            asistencia.setFechaRegistro(LocalDateTime.of(fechaLista, LocalTime.MIN)); // Solo la fecha de la lista
        }

        asistenciaRepositorio.save(asistencia);
    }
}

    public Entidad_Lista obtenerListaPorId(Long idLista) {
        return repositorioLista.findById(idLista)
                .orElseThrow(() -> new NoSuchElementException("Lista no encontrada"));
    }
}
