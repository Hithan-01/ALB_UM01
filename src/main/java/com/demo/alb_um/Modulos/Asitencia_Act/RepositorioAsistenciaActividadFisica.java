package com.demo.alb_um.Modulos.Asitencia_Act;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RepositorioAsistenciaActividadFisica extends JpaRepository<Ent_AsistenciaActividadFisica, Long> {

    // Método personalizado para encontrar la asistencia de un alumno en una lista específica
    Optional<Ent_AsistenciaActividadFisica> findByListaAndUsuarioAlumno(Entidad_Lista lista, Entidad_Usuario_Alumno usuarioAlumno);

     // Método para encontrar todas las asistencias de una lista específica
    List<Ent_AsistenciaActividadFisica> findByLista(Entidad_Lista lista);

    long countByUsuarioAlumnoAndEstadoFalta(Entidad_Usuario_Alumno usuarioAlumno, Ent_AsistenciaActividadFisica.EstadoFalta estadoFalta);
}
