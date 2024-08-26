package com.demo.alb_um.Modulos.Alumno_Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoActividadRepositorio extends JpaRepository<Ent_AlumnoActividad, AlumnoActividadId> {
}
