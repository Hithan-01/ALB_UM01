package com.demo.alb_um.Modulos.Coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachActividadRepositorio extends JpaRepository<Ent_CoachActividad, CoachActividadId> {
    List<Ent_CoachActividad> findByUsuario_UserName(String userName);
}
