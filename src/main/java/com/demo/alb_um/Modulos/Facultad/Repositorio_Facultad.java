package com.demo.alb_um.Modulos.Facultad;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
public interface Repositorio_Facultad extends JpaRepository<Entidad_facultad, Long> {

    Optional<Entidad_facultad> findByNombre(String nombre);

}

