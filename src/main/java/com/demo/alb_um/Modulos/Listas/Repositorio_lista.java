package com.demo.alb_um.Modulos.Listas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repositorio_lista extends JpaRepository<Entidad_Lista, Long> {
}
