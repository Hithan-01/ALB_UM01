package com.demo.alb_um.Modulos.Asitencia_Act;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;

import java.util.Optional;

@Repository
public interface RepositorioAsistenciaActividadFisica extends JpaRepository<Ent_AsistenciaActividadFisica, Long> {
    Optional<Ent_AsistenciaActividadFisica> findByListaAndUsuarioAlumno(Entidad_Lista lista, Entidad_Usuario_Alumno usuarioAlumno);
    
}
