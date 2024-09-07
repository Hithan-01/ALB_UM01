package com.demo.alb_um.Modulos.Listas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;

import java.util.Optional;
import java.util.Date;

@Repository
public interface Repositorio_lista extends JpaRepository<Entidad_Lista, Long> {
    Optional<Entidad_Lista> findByActividadFisicaAndFecha(Entidad_ActividadFisica actividadFisica, Date fecha);

}
