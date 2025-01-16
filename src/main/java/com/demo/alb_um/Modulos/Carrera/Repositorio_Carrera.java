package com.demo.alb_um.Modulos.Carrera;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repositorio_Carrera extends JpaRepository<Entidad_carrera, Long> {}

