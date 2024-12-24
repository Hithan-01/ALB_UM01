package com.demo.alb_um.Modulos.Coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CoachActividadRepositorio extends JpaRepository<Ent_CoachActividad, CoachActividadId> {
    List<Ent_CoachActividad> findByUsuario_UserName(String userName);

   
    @Query("SELECT ca FROM Ent_CoachActividad ca WHERE ca.actividadFisica.idActividadFisica = :idActividadFisica")
    Optional<Ent_CoachActividad> findByActividadFisicaId(@Param("idActividadFisica") Long idActividadFisica);
    
@Modifying
@Query("DELETE FROM Ent_CoachActividad ca WHERE ca.actividadFisica.idActividadFisica = :idActividadFisica")
void deleteByActividadFisicaId(@Param("idActividadFisica") Long idActividadFisica);



    
    List<Ent_CoachActividad> findByUsuario_IdUsuario(Long idUsuario);

    List<Ent_CoachActividad> findByUsuario_Rol(String rol);

    @Modifying
@Query("DELETE FROM Ent_CoachActividad ca WHERE ca.actividadFisica.idActividadFisica = :idActividadFisica")
void deleteByActividadFisica_Id(@Param("idActividadFisica") Long idActividadFisica);

}
