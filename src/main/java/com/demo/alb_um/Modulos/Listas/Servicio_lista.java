package com.demo.alb_um.Modulos.Listas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;

import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;

import java.time.LocalDate;
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

public void guardarAsistencia(Entidad_Lista lista, List<Long> asistencias) {
    // Obtener todos los alumnos inscritos en la actividad de la lista
    Set<Ent_AlumnoActividad> alumnosActividad = lista.getActividadFisica().getAlumnoActividades();
    
    for (Ent_AlumnoActividad alumnoActividad : alumnosActividad) {
        // Buscar si ya existe un registro de asistencia para el alumno y la lista
        Optional<Ent_AsistenciaActividadFisica> asistenciaExistente = asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, alumnoActividad.getUsuarioAlumno());
        
        Ent_AsistenciaActividadFisica asistencia;
        
        if (asistenciaExistente.isPresent()) {
            // Si ya existe, actualizar la asistencia
            asistencia = asistenciaExistente.get();
        } else {
            // Si no existe, crear una nueva asistencia
            asistencia = new Ent_AsistenciaActividadFisica();
            asistencia.setLista(lista);
            asistencia.setUsuarioAlumno(alumnoActividad.getUsuarioAlumno());
        }
        
        // Actualizar el estado de asistencia (presente o no)
        asistencia.setPresente(asistencias.contains(alumnoActividad.getUsuarioAlumno().getIdUsuarioAlumno()));
        asistenciaRepositorio.save(asistencia); // Guardar o actualizar la asistencia
    }
}

public Entidad_Lista obtenerListaPorId(Long idLista) {
    return repositorioLista.findById(idLista)
            .orElseThrow(() -> new NoSuchElementException("Lista no encontrada"));
}



}
