package com.demo.alb_um.Modulos.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Servicio_Servicio {

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    public List<Ent_Servicio> obtenerTodosLosServicios() {
        return servicioRepositorio.findAll();
    }

    public Optional<Ent_Servicio> obtenerServicioPorId(Long id) {
        return servicioRepositorio.findById(id);
    }

    public Ent_Servicio guardarServicio(Ent_Servicio servicio) {
        return servicioRepositorio.save(servicio);
    }

    public void eliminarServicio(Long id) {
        servicioRepositorio.deleteById(id);
    }
}
