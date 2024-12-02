package com.demo.alb_um.Modulos.Antropometria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AntropometriaRepositorio extends JpaRepository<Ent_Antro, Long> {

    /**
     * Busca los datos antropométricos por el ID de la cita.
     * 
     * @param idCita ID de la cita.
     * @return Un Optional con los datos antropométricos asociados a la cita.
     */
    @Query("SELECT a FROM Ent_Antro a WHERE a.cita.idCita = :idCita")
    Optional<Ent_Antro> findByCitaId(@Param("idCita") Long idCita);

    /**
     * Verifica si ya existen datos antropométricos asociados a una cita.
     * 
     * @param idCita ID de la cita.
     * @return True si existen datos, False en caso contrario.
     */
    @Query("SELECT COUNT(a) > 0 FROM Ent_Antro a WHERE a.cita.idCita = :idCita")
    boolean existsByCitaId(@Param("idCita") Long idCita);

    /**
     * Busca datos antropométricos por el ID del usuario alumno.
     * 
     * @param idUsuarioAlumno ID del alumno.
     * @return Un Optional con los datos antropométricos asociados al alumno.
     */
    Optional<Ent_Antro> findByUsuarioAlumno_IdUsuarioAlumno(Long idUsuarioAlumno);

    /**
     * Lista los datos antropométricos de un alumno ordenados por la fecha del día
     * del horario de servicio, de forma descendente.
     * 
     * @param idUsuarioAlumno ID del alumno.
     * @return Lista de datos antropométricos ordenados por fecha.
     */
    List<Ent_Antro> findByUsuarioAlumno_IdUsuarioAlumnoOrderByCita_HorarioServicio_DiaSemanaDesc(Long idUsuarioAlumno);
}
