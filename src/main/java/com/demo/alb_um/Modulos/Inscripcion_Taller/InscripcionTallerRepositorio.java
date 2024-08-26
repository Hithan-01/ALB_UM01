package com.demo.alb_um.Modulos.Inscripcion_Taller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionTallerRepositorio extends JpaRepository<Ent_InscripcionTaller, Long> {
    List<Ent_InscripcionTaller> findByUsuarioAlumno_Usuario_UserName(String userName);
}
