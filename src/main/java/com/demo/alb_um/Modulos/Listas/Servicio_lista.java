package com.demo.alb_um.Modulos.Listas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    
        // Validar el día y la hora usando el identificador
        if (!esDiaCorrecto(identificadorGrupo, diaActual, horaProgramada)) {
            throw new IllegalArgumentException("No es el día o la hora correspondiente para iniciar la lista.");
        }
    
        // Validar si la hora actual está dentro del rango permitido
        LocalTime horaActual = LocalTime.now();
        LocalTime horaInicio = horaProgramada.minusMinutes(10); // 10 minutos antes
        LocalTime horaCierre = horaProgramada.plusMinutes(40);  // 40 minutos después
    
        // Logs de validación de tiempo
        System.out.println("Hora actual: " + horaActual);
        System.out.println("Hora programada: " + horaProgramada);
        System.out.println("Rango permitido: " + horaInicio + " - " + horaCierre);
    
        if (horaActual.isBefore(horaInicio)) {
            throw new IllegalArgumentException("Espera a la hora indicada de la clase para iniciar el pase de lista.");
        }
    
        if (horaActual.isAfter(horaCierre)) {
            throw new IllegalArgumentException("El tiempo para iniciar la lista ha expirado.");
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
public synchronized void guardarAsistencia(Entidad_Lista lista, List<Long> asistencias, LocalDateTime horaActual) {
    Set<Ent_AlumnoActividad> alumnosActividad = lista.getActividadFisica().getAlumnoActividades();

    LocalTime horaInicioActividad = lista.getActividadFisica().getHora().toLocalTime();
    int asistenciaLimiteMinutos = 10; // Parametrizable
    LocalDateTime horaLimite = LocalDate.now().atTime(horaInicioActividad).plusMinutes(asistenciaLimiteMinutos);

    // Obtener asistencia existente de la lista y mapearla
    Map<Long, Ent_AsistenciaActividadFisica> asistenciaMap = asistenciaRepositorio.findByLista(lista).stream()
            .collect(Collectors.toMap(
                    asistencia -> asistencia.getUsuarioAlumno().getIdUsuarioAlumno(),
                    asistencia -> asistencia
            ));

    for (Ent_AlumnoActividad alumnoActividad : alumnosActividad) {
        Long alumnoId = alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno();
        Ent_AsistenciaActividadFisica asistencia = asistenciaMap.getOrDefault(alumnoId, new Ent_AsistenciaActividadFisica());

        boolean estaPresente = asistencias.contains(alumnoId);

        // Actualizar asistencia solo si está presente
        if (estaPresente) {
            if (asistencia.isPresente()) {
                continue; // Saltar si ya está registrado como presente
            }
            
            if (asistencia.getFechaRegistro() == null) {
                asistencia.setFechaRegistro(horaActual); // Registrar la hora actual si no existe
            }

            asistencia.setPresente(horaActual.isBefore(horaLimite) || horaActual.isEqual(horaLimite));
            asistencia.setLista(lista);
            asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());
            asistenciaRepositorio.save(asistencia); // Guardar la asistencia
        } else {
            // Marcar como ausente
            if (asistencia.getFechaRegistro() == null) {
                asistencia.setFechaRegistro(horaActual); // Registrar evaluación
            }

            asistencia.setPresente(false);
            asistencia.setLista(lista);
            asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());
            asistenciaRepositorio.save(asistencia);
        }
    }
}


    public Entidad_Lista obtenerListaPorId(Long idLista) {
        return repositorioLista.findById(idLista)
                .orElseThrow(() -> new NoSuchElementException("Lista no encontrada"));
    }
}
