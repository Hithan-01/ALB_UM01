package com.demo.alb_um.Modulos.Citas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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



}
