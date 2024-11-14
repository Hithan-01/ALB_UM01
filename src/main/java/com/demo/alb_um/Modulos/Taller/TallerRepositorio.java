package com.demo.alb_um.Modulos.Taller;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;

@Repository
public interface TallerRepositorio extends JpaRepository<Ent_Taller, Long> {
      // Buscar talleres con cupos disponibles mayores a cero
      List<Ent_Taller> findByCuposDisponiblesGreaterThan(int cuposDisponibles);

      List<Ent_Taller> findByFechaAndHoraBeforeAndDuracion(LocalDate fecha, LocalTime hora, Integer duracion);


    
    // Buscar talleres por fecha y estado
    List<Ent_Taller> findByFechaAndEstado(LocalDate fecha, EstadoTaller estado);
    
    // Buscar talleres programados para hoy que aún no han iniciado
    List<Ent_Taller> findByFechaAndEstadoOrderByHora(LocalDate fecha, EstadoTaller estado);
    
   

     // Método para encontrar por fecha SQL
    List<Ent_Taller> findByFecha(Date fecha);
    
    
    @Query("SELECT t FROM Ent_Taller t WHERE t.estado = :estado")
    List<Ent_Taller> findByEstado(@Param("estado") EstadoTaller estado);
    // Query específica para convertir java.sql.Date a la fecha actual
    @Query("SELECT t FROM Ent_Taller t WHERE CAST(t.fecha AS date) = CURRENT_DATE")
    List<Ent_Taller> findTalleresDelDia();




    

    @Query(value = "SELECT * FROM taller t " +
           "WHERE DATE(t.fecha) = CAST(CURRENT_DATE AS DATE) " +
           "AND t.estado = :estado " +
           "ORDER BY t.hora ASC", 
           nativeQuery = true)
    List<Ent_Taller> findByFechaAndEstado(@Param("estado") String estado);


    @Query(value = "SELECT t.* FROM taller t " +
    "WHERE t.fecha = :fecha " +
    "ORDER BY t.hora ASC", 
    nativeQuery = true)
List<Ent_Taller> findByFecha(@Param("fecha") LocalDate fecha);

// Query adicional para debugging
@Query(value = "SELECT t.*, DATE(t.fecha) as fecha_formateada, CURRENT_DATE as fecha_actual " +
    "FROM taller t", 
    nativeQuery = true)
List<Object[]> findAllWithDates();
}
