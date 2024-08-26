package com.demo.alb_um.Modulos.Horario_servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioServicioServicio {

    @Autowired
    private HorarioServicioRepositorio horarioServicioRepositorio;

    public List<Ent_HorarioServicio> obtenerTodosLosHorariosServicio() {
        return horarioServicioRepositorio.findAll();
    }

    public Optional<Ent_HorarioServicio> obtenerHorarioServicioPorId(Long id) {
        return horarioServicioRepositorio.findById(id);
    }

    public Ent_HorarioServicio guardarHorarioServicio(Ent_HorarioServicio horarioServicio) {
        return horarioServicioRepositorio.save(horarioServicio);
    }

    public void eliminarHorarioServicio(Long id) {
        horarioServicioRepositorio.deleteById(id);
    }
}
