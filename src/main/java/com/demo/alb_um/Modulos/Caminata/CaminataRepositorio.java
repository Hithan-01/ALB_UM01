package com.demo.alb_um.Modulos.Caminata;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;

@Repository
public interface CaminataRepositorio extends JpaRepository<Entidad_Caminata, Long> {
    Entidad_Caminata findByAsistenciaIdAsistenciaActividadFisica(Long idAsistencia);
        Optional<Entidad_Caminata> findByAsistencia(Ent_AsistenciaActividadFisica asistencia);
}
