package com.demo.alb_um.Modulos.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;

import java.util.Optional;

@Repository
public interface UsuarioAlumnoRepositorio extends JpaRepository<Entidad_Usuario_Alumno, Long>, UsuarioAlumnoRepositorioCustom {
    Optional<Entidad_Usuario_Alumno> findByUsuario_UserName(String userName);
    Optional<Entidad_Usuario_Alumno> findByUsuario(Entidad_Usuario usuario);
}


