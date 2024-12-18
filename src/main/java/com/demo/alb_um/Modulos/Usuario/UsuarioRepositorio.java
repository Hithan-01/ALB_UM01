package com.demo.alb_um.Modulos.Usuario;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Entidad_Usuario, Long> {
    Optional<Entidad_Usuario> findByUserName(String userName);
    List<Entidad_Usuario> findByContrasenaNotLike(String pattern);
    List<Entidad_Usuario> findByRol(String rol);
}
