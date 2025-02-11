package com.demo.alb_um.Modulos.Carrera;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repositorio_Carrera extends JpaRepository<Entidad_carrera, Long> {

    Optional<Entidad_carrera> findByNombre(String nombre);

}

