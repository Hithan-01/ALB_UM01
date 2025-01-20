package com.demo.alb_um.Modulos.Citas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepositorio extends JpaRepository<Ent_Cita, Long> {
    // Método para obtener citas verificadas
    List<Ent_Cita> findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(String userName);

    // Método para obtener todas las citas por username (sin filtrar por verificación)
    List<Ent_Cita> findByUsuarioAlumno_Usuario_UserName(String userName);

    // Método para obtener citas no verificadas
List<Ent_Cita> findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(String userName);

List<Ent_Cita> findByUsuarioAdmin_Servicio_IdServicioAndVerificacionFalse(Long servicioId);

boolean existsByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_Nombre(Long idAlumno, String nombreServicio);

 @Query("SELECT c FROM Ent_Cita c WHERE c.horarioServicio.diaSemana = :fecha " +
           "AND c.verificacion = false " +
           "AND c.usuarioAdmin.servicio.idServicio = :servicioId")
    List<Ent_Cita> findByFechaAndServicioAndPendientes(
        @Param("fecha") LocalDate fecha,
        @Param("servicioId") Long servicioId
    );

    List<Ent_Cita> findByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_NombreAndEstadoAsistenciaAndVerificacionTrue(
    Long idUsuarioAlumno, String nombreServicio, String estadoAsistencia);

    @Query("SELECT c FROM Ent_Cita c WHERE c.usuarioAlumno.idUsuarioAlumno = :idUsuarioAlumno " +
       "AND c.horarioServicio.servicio.nombre = :nombreServicio " +
       "AND c.estadoAsistencia = :estadoAsistencia " +
       "AND c.verificacion = TRUE")
List<Ent_Cita> findCitasConfirmadasByUsuarioAndServicio(
    @Param("idUsuarioAlumno") Long idUsuarioAlumno,
    @Param("nombreServicio") String nombreServicio,
    @Param("estadoAsistencia") String estadoAsistencia);

    boolean existsByUsuarioAlumno_IdUsuarioAlumnoAndHorarioServicio_Servicio_IdServicioAndVerificacionTrue(Long alumnoId, Long servicioId);


}
