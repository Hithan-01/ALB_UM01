package com.demo.alb_um.Modulos.Taller;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallerRepositorio extends JpaRepository<Ent_Taller, Long> {
      // Buscar talleres con cupos disponibles mayores a cero
      List<Ent_Taller> findByCuposDisponiblesGreaterThan(int cuposDisponibles);
}
