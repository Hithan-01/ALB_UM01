package com.demo.alb_um.Modulos.Listas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;

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

    // Obtener o crear lista
    public Entidad_Lista obtenerOCrearLista(ActividadFisicaDTO actividadDTO, LocalDate fechaActual) {
        Date fecha = Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Entidad_ActividadFisica actividadFisica = convertirADesdeDTO(actividadDTO);

        // Verificar si ya existe una lista para esa actividad y fecha
        return repositorioLista.findByActividadFisicaAndFecha(actividadFisica, fecha)
                .orElseGet(() -> {
                    Entidad_Lista nuevaLista = new Entidad_Lista();
                    nuevaLista.setFecha(fecha);
                    nuevaLista.setActividadFisica(actividadFisica);
                    return repositorioLista.save(nuevaLista);
                });
    }

    private Entidad_ActividadFisica convertirADesdeDTO(ActividadFisicaDTO actividadDTO) {
        Entidad_ActividadFisica entidad = new Entidad_ActividadFisica();
        entidad.setIdActividadFisica(actividadDTO.getIdActividadFisica());
        entidad.setNombre(actividadDTO.getNombre());
        return entidad;
    }

    // Guardar asistencia
    public void guardarAsistencia(Entidad_Lista lista, List<Long> asistencias, LocalDateTime horaActual) {
        Set<Ent_AlumnoActividad> alumnosActividad = lista.getActividadFisica().getAlumnoActividades();

        LocalTime horaInicioActividad = lista.getActividadFisica().getHora().toLocalTime();
        LocalDateTime horaLimite = LocalDate.now().atTime(horaInicioActividad).plusMinutes(10);

        // Obtener asistencia existente de la lista y mapearla
        Map<Long, Ent_AsistenciaActividadFisica> asistenciaMap = asistenciaRepositorio.findByLista(lista).stream()
                .collect(Collectors.toMap(asistencia -> asistencia.getUsuarioAlumno().getIdUsuarioAlumno(), asistencia -> asistencia));

        for (Ent_AlumnoActividad alumnoActividad : alumnosActividad) {
            Long alumnoId = alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno();
            Ent_AsistenciaActividadFisica asistencia = asistenciaMap.getOrDefault(alumnoId, new Ent_AsistenciaActividadFisica());

            boolean estaPresente = asistencias.contains(alumnoId);

            // Crear o actualizar la asistencia solo si está presente
            if (estaPresente) {
                if (asistencia.getFechaRegistro() == null) {
                    asistencia.setFechaRegistro(horaActual); // Registrar la hora actual si no existe
                }

                asistencia.setPresente(horaActual.isBefore(horaLimite) || horaActual.isEqual(horaLimite));
                asistencia.setLista(lista);
                asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());
                asistenciaRepositorio.save(asistencia); // Guardar la asistencia
            }
        }
    }

    public Entidad_Lista obtenerListaPorId(Long idLista) {
        return repositorioLista.findById(idLista)
                .orElseThrow(() -> new NoSuchElementException("Lista no encontrada"));
    }
}
