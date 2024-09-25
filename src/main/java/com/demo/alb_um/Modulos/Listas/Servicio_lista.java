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

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class Servicio_lista {

    private final Repositorio_lista repositorioLista;

    @Autowired
    public Servicio_lista(Repositorio_lista repositorioLista) {
        this.repositorioLista = repositorioLista;
    }

   
public Entidad_Lista obtenerOCrearLista(ActividadFisicaDTO actividadDTO, LocalDate fechaActual) {
    // Convertir LocalDate a Date
    Date fecha = Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant());

    Entidad_ActividadFisica actividadFisica = convertirADesdeDTO(actividadDTO);

    // Verificar si ya existe una lista para esa actividad y fecha
    Optional<Entidad_Lista> listaExistente = repositorioLista.findByActividadFisicaAndFecha(actividadFisica, fecha);

    if (listaExistente.isPresent()) {
        return listaExistente.get();
    } else {
        // Crear una nueva lista si no existe
        Entidad_Lista nuevaLista = new Entidad_Lista();
        nuevaLista.setFecha(fecha);
        nuevaLista.setActividadFisica(actividadFisica);
        return repositorioLista.save(nuevaLista);
    }
}
    
    
    // Método para convertir el DTO a entidad (si no tienes acceso directo desde el DTO)
    private Entidad_ActividadFisica convertirADesdeDTO(ActividadFisicaDTO actividadDTO) {
        Entidad_ActividadFisica entidad = new Entidad_ActividadFisica();
        entidad.setIdActividadFisica(actividadDTO.getIdActividadFisica());
        entidad.setNombre(actividadDTO.getNombre());
        // Configura otros atributos que necesites
        return entidad;
    }
    
@Autowired
private RepositorioAsistenciaActividadFisica asistenciaRepositorio;
public void guardarAsistencia(Entidad_Lista lista, List<Long> asistencias, LocalDateTime horaActual) {
    Set<Ent_AlumnoActividad> alumnosActividad = lista.getActividadFisica().getAlumnoActividades();

    for (Ent_AlumnoActividad alumnoActividad : alumnosActividad) {
        Optional<Ent_AsistenciaActividadFisica> asistenciaExistente = asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, alumnoActividad.getUsuarioAlumno());

        Ent_AsistenciaActividadFisica asistencia;

        if (asistenciaExistente.isPresent()) {
            asistencia = asistenciaExistente.get();

            // Verificar si el alumno está en la lista de asistencias enviada (marcado como presente)
            boolean estaPresente = asistencias.contains(alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno());

            // Solo registrar la hora actual si la fecha de registro es nula y el alumno está presente
            if (estaPresente && asistencia.getFechaRegistro() == null) {
                asistencia.setFechaRegistro(horaActual);  // Registrar la hora actual solo si no se ha registrado antes y está presente
            }

            // Actualizar el estado de "presente" sin importar la hora
            asistencia.setPresente(estaPresente);  // Se marca como presente si está en la lista de presentes

            asistenciaRepositorio.save(asistencia);
        } else {
            // Si no existe asistencia previa, crear una nueva solo si el alumno está presente
            if (asistencias.contains(alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno())) {
                asistencia = new Ent_AsistenciaActividadFisica();
                asistencia.setLista(lista);
                asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());
                asistencia.setFechaRegistro(horaActual);  // Registrar la hora actual
                asistencia.setPresente(true);  // Marcar como presente sin importar la hora
                asistenciaRepositorio.save(asistencia);
            }
        }
    }
}


public Entidad_Lista obtenerListaPorId(Long idLista) {
    return repositorioLista.findById(idLista)
            .orElseThrow(() -> new NoSuchElementException("Lista no encontrada"));
}



}
