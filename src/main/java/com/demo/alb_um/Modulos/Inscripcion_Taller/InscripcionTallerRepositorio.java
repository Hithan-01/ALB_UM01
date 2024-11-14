package com.demo.alb_um.Modulos.Inscripcion_Taller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionTallerRepositorio extends JpaRepository<Ent_InscripcionTaller, Long> {
    // Consultas existentes
    List<Ent_InscripcionTaller> findByUsuarioAlumno_Usuario_UserNameAndVerificacionTrue(String userName);
    List<Ent_InscripcionTaller> findByUsuarioAlumno_Usuario_UserName(String userName);
    List<Ent_InscripcionTaller> findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(String userName);
    List<Ent_InscripcionTaller> findByTaller_IdTaller(Long idTaller);
    List<Ent_InscripcionTaller> findByTaller_IdTallerAndVerificacionFalse(Long idTaller);

    // Verificaciones de existencia
    boolean existsByUsuarioAlumno_IdUsuarioAlumnoAndTaller_IdTaller(Long idAlumno, Long idTaller);
    boolean existsByUsuarioAlumno_Usuario_UserNameAndTaller_IdTaller(String userName, Long idTaller);

    // Consultas optimizadas para registro de llegada
    @Query("SELECT i FROM Ent_InscripcionTaller i " +
           "JOIN FETCH i.usuarioAlumno ua " +
           "JOIN FETCH ua.usuario u " +
           "WHERE i.taller.idTaller = :tallerId " +
           "AND UPPER(u.userName) = UPPER(:userName)")
    Optional<Ent_InscripcionTaller> findByTaller_IdTallerAndUsuarioAlumno_Usuario_UserName(
            @Param("tallerId") Long tallerId, 
            @Param("userName") String userName);

    @Query("SELECT i FROM Ent_InscripcionTaller i " +
           "JOIN FETCH i.usuarioAlumno ua " +
           "JOIN FETCH ua.usuario u " +
           "WHERE i.taller.idTaller = :tallerId " +
           "AND u.tagCredencial = :tagCredencial")
    Optional<Ent_InscripcionTaller> findByTaller_IdTallerAndUsuarioAlumno_Usuario_TagCredencial(
            @Param("tallerId") Long tallerId, 
            @Param("tagCredencial") String tagCredencial);

            

    @Query("SELECT i FROM Ent_InscripcionTaller i " +
           "JOIN FETCH i.usuarioAlumno ua " +
           "JOIN FETCH ua.usuario u " +
           "WHERE i.taller.idTaller = :tallerId " +
           "AND (UPPER(u.userName) = UPPER(:identificador) OR u.tagCredencial = :identificador)")
    Optional<Ent_InscripcionTaller> findByTallerAndIdentificador(
            @Param("tallerId") Long tallerId, 
            @Param("identificador") String identificador);

    // Query para debugging
    @Query(value = "SELECT u.user_name, u.tag_credencial " +
                   "FROM usuario u " +
                   "JOIN usuario_alumno ua ON u.id_usuario = ua.usuario_id " +
                   "JOIN inscripcion_taller it ON ua.id_usuario_alumno = it.id_usuario_alumno " +
                   "WHERE it.id_taller = :tallerId", 
           nativeQuery = true)
    List<Object[]> findAllInscritosEnTaller(@Param("tallerId") Long tallerId);


    
}