package com.demo.alb_um.Modulos.Horario_servicio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.DTOs.HorarioServicioDTO;

@Repository
public interface HorarioServicioRepositorio extends JpaRepository<Ent_HorarioServicio, Long> {
    Optional<Ent_HorarioServicio> findFirstByServicio_IdServicio(Long idServicio);


   
    @Query("SELECT new com.demo.alb_um.DTOs.HorarioServicioDTO(h.idHorarioServicio, h.servicio.id, h.diaSemana, h.hora, h.disponible) " +
    "FROM Ent_HorarioServicio h " +
    "WHERE h.disponible = true AND h.diaSemana IN :fechasFacultad")
List<HorarioServicioDTO> findHorariosDisponiblesPorFacultad(@Param("fechasFacultad") List<LocalDate> fechasFacultad);

 // Nuevo método: Buscar el horario y cargar el servicio y el administrador asociados
 @Query("SELECT h FROM Ent_HorarioServicio h JOIN FETCH h.servicio s JOIN FETCH s.usuariosAdmin ua WHERE h.idHorarioServicio = :horarioId")
Optional<Ent_HorarioServicio> findHorarioConAdminPorId(@Param("horarioId") Long horarioId);

}
