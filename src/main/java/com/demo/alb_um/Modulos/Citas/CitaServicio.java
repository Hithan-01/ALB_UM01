package com.demo.alb_um.Modulos.Citas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaServicio {

    @Autowired
    private CitaRepositorio citaRepositorio;

    public List<Ent_Cita> obtenerTodasLasCitas() {
        return citaRepositorio.findAll();
    }

    public Optional<Ent_Cita> obtenerCitaPorId(Long id) {
        return citaRepositorio.findById(id);
    }

    public Ent_Cita guardarCita(Ent_Cita cita) {
        return citaRepositorio.save(cita);
    }

    public void eliminarCita(Long id) {
        citaRepositorio.deleteById(id);
    }
}
