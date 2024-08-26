package com.demo.alb_um.Modulos.Actividad_Fisica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadFisicaRepositorio extends JpaRepository<Entidad_ActividadFisica, Long> {
}
