package com.demo.alb_um.Modulos.Taller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TallerServicio {

    @Autowired
    private TallerRepositorio tallerRepositorio;

    public List<Ent_Taller> obtenerTodosLosTalleres() {
        return tallerRepositorio.findAll();
    }

    public Optional<Ent_Taller> obtenerTallerPorId(Long id) {
        return tallerRepositorio.findById(id);
    }

    public Ent_Taller guardarTaller(Ent_Taller taller) {
        return tallerRepositorio.save(taller);
    }

    public void eliminarTaller(Long id) {
        tallerRepositorio.deleteById(id);
    }
}
