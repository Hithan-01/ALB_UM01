package com.demo.alb_um.Modulos.Horario_servicio;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioServicioRepositorio extends JpaRepository<Ent_HorarioServicio, Long> {
    Optional<Ent_HorarioServicio> findFirstByServicio_IdServicio(Long idServicio);
}
