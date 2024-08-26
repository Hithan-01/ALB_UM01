package com.demo.alb_um.Modulos.Asitencia_Act;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAsistenciaActividadFisica extends JpaRepository<Ent_AsistenciaActividadFisica, Long> {
}
