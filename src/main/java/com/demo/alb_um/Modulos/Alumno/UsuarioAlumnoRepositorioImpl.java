package com.demo.alb_um.Modulos.Alumno;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.DTOs.AlumnoBusquedaDTO;
import com.demo.alb_um.DTOs.BusquedaFaltas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UsuarioAlumnoRepositorioImpl implements UsuarioAlumnoRepositorioCustom {

    @PersistenceContext
    private EntityManager entityManager;

@Override
public List<AlumnoBusquedaDTO> busquedaAvanzada(Long idFacultad, Long idCarrera, Integer numeroFaltas, 
                                                String semestre, String filtroFaltas) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<AlumnoBusquedaDTO> query = cb.createQuery(AlumnoBusquedaDTO.class);
    Root<Entidad_Usuario_Alumno> alumno = query.from(Entidad_Usuario_Alumno.class);

    // Relaciones
    Join<Object, Object> carrera = alumno.join("carrera");
    Join<Object, Object> facultad = carrera.join("facultad");
    Join<Object, Object> alumnoActividad = alumno.join("alumnoActividades", JoinType.LEFT);
    Join<Object, Object> actividad = alumnoActividad.join("actividadFisica", JoinType.LEFT);

    // Subconsultas
    Subquery<Long> subqueryFaltas = query.subquery(Long.class);
    Root<Ent_AsistenciaActividadFisica> subAsistenciasFaltas = subqueryFaltas.from(Ent_AsistenciaActividadFisica.class);
    subqueryFaltas.select(cb.count(subAsistenciasFaltas))
        .where(cb.and(
            cb.equal(subAsistenciasFaltas.get("usuarioAlumno"), alumno),
            cb.equal(subAsistenciasFaltas.get("estadoFalta"), Ent_AsistenciaActividadFisica.EstadoFalta.FALTA)
        ));

    Subquery<Long> subqueryAsistencias = query.subquery(Long.class);
    Root<Ent_AsistenciaActividadFisica> subAsistenciasTotal = subqueryAsistencias.from(Ent_AsistenciaActividadFisica.class);
    subqueryAsistencias.select(cb.count(subAsistenciasTotal))
        .where(cb.and(
            cb.equal(subAsistenciasTotal.get("usuarioAlumno"), alumno),
            cb.equal(subAsistenciasTotal.get("estadoFalta"), Ent_AsistenciaActividadFisica.EstadoFalta.PRESENTE)
        ));

    Subquery<Long> subqueryJustificaciones = query.subquery(Long.class);
    Root<Ent_AsistenciaActividadFisica> subAsistenciasJustificaciones = subqueryJustificaciones.from(Ent_AsistenciaActividadFisica.class);
    subqueryJustificaciones.select(cb.count(subAsistenciasJustificaciones))
        .where(cb.and(
            cb.equal(subAsistenciasJustificaciones.get("usuarioAlumno"), alumno),
            cb.equal(subAsistenciasJustificaciones.get("estadoFalta"), Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA)
        ));

    // Predicados
    List<Predicate> predicates = new ArrayList<>();

    if (idFacultad != null) {
        predicates.add(cb.equal(facultad.get("idFacultad"), idFacultad));
    }
    if (idCarrera != null) {
        predicates.add(cb.equal(carrera.get("idCarrera"), idCarrera));
    }
    if (numeroFaltas != null) {
        predicates.add(cb.equal(subqueryFaltas, numeroFaltas.longValue()));
    }
    if (semestre != null && !semestre.isEmpty()) {
        predicates.add(cb.equal(alumno.get("semestre"), semestre));
    }
    if ("CON_FALTAS".equalsIgnoreCase(filtroFaltas)) {
        predicates.add(cb.greaterThan(subqueryFaltas, 0L));
    } else if ("SIN_FALTAS".equalsIgnoreCase(filtroFaltas)) {
        predicates.add(cb.equal(subqueryFaltas, 0L));
    } else if ("CON_JUSTIFICACIONES".equalsIgnoreCase(filtroFaltas)) {
        predicates.add(cb.greaterThan(subqueryJustificaciones, 0L));
    }

    query.select(cb.construct(
        AlumnoBusquedaDTO.class,
        alumno.get("idUsuarioAlumno").alias("idAlumno"),
        cb.concat(alumno.get("usuario").get("nombre"), cb.concat(" ", alumno.get("usuario").get("apellido"))).alias("nombreCompleto"),
        carrera.get("nombre").alias("carrera"),
        facultad.get("nombre").alias("facultad"),
        subqueryFaltas.alias("contadorFaltas"),
        subqueryAsistencias.alias("contadorAsistencias"),
        subqueryJustificaciones.alias("contadorJustificaciones"),
        alumno.get("semestre").alias("semestre"),
        actividad.get("idActividadFisica").alias("idActividadFisica")
    ))
    .where(predicates.toArray(new Predicate[0]))
    .groupBy(alumno.get("idUsuarioAlumno"), actividad.get("idActividadFisica"));
    


    return entityManager.createQuery(query).getResultList();
}

    
@Override
public List<BusquedaFaltas> buscarFaltasPorFiltros(Date fecha, Long idFacultad, Long idCarrera, String semestre) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<BusquedaFaltas> query = cb.createQuery(BusquedaFaltas.class);
    Root<Ent_AsistenciaActividadFisica> asistencia = query.from(Ent_AsistenciaActividadFisica.class);

    // Relaciones necesarias
    Join<Object, Object> alumno = asistencia.join("usuarioAlumno");
    Join<Object, Object> carrera = alumno.join("carrera");
    Join<Object, Object> facultad = carrera.join("facultad");
    Join<Object, Object> lista = asistencia.join("lista");
    Join<Object, Object> actividad = lista.join("actividadFisica");

    // Predicados para los filtros
    List<Predicate> predicates = new ArrayList<>();

    // Filtro por fecha
    if (fecha != null) {
        predicates.add(cb.equal(lista.get("fecha"), fecha));
    }

    // Filtro por facultad
    if (idFacultad != null) {
        predicates.add(cb.equal(facultad.get("idFacultad"), idFacultad));
    }

    // Filtro por carrera
    if (idCarrera != null) {
        predicates.add(cb.equal(carrera.get("idCarrera"), idCarrera));
    }

    // Filtro por semestre
    if (semestre != null && !semestre.isEmpty()) {
        predicates.add(cb.equal(alumno.get("semestre"), semestre));
    }

    // Solo registros con estado "FALTA"
    predicates.add(cb.equal(asistencia.get("estadoFalta"), Ent_AsistenciaActividadFisica.EstadoFalta.FALTA));

    // Construcción del DTO
    query.select(cb.construct(
        BusquedaFaltas.class,
        asistencia.get("idAsistenciaActividadFisica"), // ID único
        cb.concat(alumno.get("usuario").get("nombre"), cb.concat(" ", alumno.get("usuario").get("apellido"))), // Nombre completo
        alumno.get("usuario").get("userName"), // Matrícula
        carrera.get("nombre"), // Carrera
        facultad.get("nombre"), // Facultad
        lista.get("fecha"), // Fecha de la falta
        actividad.get("nombre"), // Nombre de la actividad
        alumno.get("semestre") // Semestre
    ))
    .where(predicates.toArray(new Predicate[0]))
    .orderBy(cb.asc(lista.get("fecha")));

    return entityManager.createQuery(query).getResultList();
}


    
}
