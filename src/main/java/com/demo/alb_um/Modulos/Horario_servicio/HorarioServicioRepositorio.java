package com.demo.alb_um.Modulos.Horario_servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioServicioRepositorio extends JpaRepository<Ent_HorarioServicio, Long> {
}
