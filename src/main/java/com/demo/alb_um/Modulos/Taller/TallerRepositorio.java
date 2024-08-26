package com.demo.alb_um.Modulos.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallerRepositorio extends JpaRepository<Ent_Taller, Long> {
}
