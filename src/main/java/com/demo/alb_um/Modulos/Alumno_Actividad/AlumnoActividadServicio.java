package com.demo.alb_um.Modulos.Alumno_Actividad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoActividadServicio {

    @Autowired
    private AlumnoActividadRepositorio alumnoActividadRepositorio;

    public List<Ent_AlumnoActividad> obtenerTodasLasAlumnoActividades() {
        return alumnoActividadRepositorio.findAll();
    }
}
