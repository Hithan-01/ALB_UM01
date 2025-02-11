package com.demo.alb_um.Modulos.Usuario;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Entidad_Usuario, Long> {
    Optional<Entidad_Usuario> findByUserName(String userName);
    List<Entidad_Usuario> findByContrasenaNotLike(String pattern);
    List<Entidad_Usuario> findByRol(String rol);

    @Query("SELECT u FROM Entidad_Usuario u WHERE u.rol = :rol AND u.idUsuario <> :idUsuario")
    List<Entidad_Usuario> findByRolAndIdUsuarioNot(@Param("rol") String rol, @Param("idUsuario") Long idUsuario);
    
    @Query("SELECT u.email FROM Entidad_Usuario u WHERE u.rol = 'ALUMNO'")
    List<String> obtenerEmailsDeAlumnos();
}
