package com.demo.alb_um.Modulos.Inscripcion_Taller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InscripcionTallerServicio {

    @Autowired
    private InscripcionTallerRepositorio inscripcionTallerRepositorio;

    public List<Ent_InscripcionTaller> obtenerTodasLasInscripciones() {
        return inscripcionTallerRepositorio.findAll();
    }

    public Optional<Ent_InscripcionTaller> obtenerInscripcionPorId(Long id) {
        return inscripcionTallerRepositorio.findById(id);
    }

    public Ent_InscripcionTaller guardarInscripcion(Ent_InscripcionTaller inscripcionTaller) {
        return inscripcionTallerRepositorio.save(inscripcionTaller);
    }

    public void eliminarInscripcion(Long id) {
        inscripcionTallerRepositorio.deleteById(id);
    }
}
