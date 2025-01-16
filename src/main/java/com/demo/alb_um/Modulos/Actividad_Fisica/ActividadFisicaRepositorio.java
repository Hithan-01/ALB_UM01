package com.demo.alb_um.Modulos.Actividad_Fisica;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadFisicaRepositorio extends JpaRepository<Entidad_ActividadFisica, Long> {

    @Query("SELECT a FROM Entidad_ActividadFisica a WHERE " +
       "LOWER(a.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
       "LOWER(a.grupo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
       "LOWER(a.diaSemana) LIKE LOWER(CONCAT('%', :filtro, '%'))")
List<Entidad_ActividadFisica> buscarPorFiltro(@Param("filtro") String filtro);

}
