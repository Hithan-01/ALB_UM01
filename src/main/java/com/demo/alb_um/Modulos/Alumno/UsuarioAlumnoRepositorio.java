package com.demo.alb_um.Modulos.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioAlumnoRepositorio extends JpaRepository<Entidad_Usuario_Alumno, Long> {
    Optional<Entidad_Usuario_Alumno> findByUsuario_UserName(String userName);
}
