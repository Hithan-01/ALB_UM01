package com.demo.alb_um.Modulos.Citas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepositorio extends JpaRepository<Ent_Cita, Long> {
    List<Ent_Cita> findByUsuarioAlumno_Usuario_UserName(String userName);
}
