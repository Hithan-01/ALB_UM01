package com.demo.alb_um.Modulos.Alumno_Actividad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alumnoActividades")
public class AlumnoActividadControlador {

    @Autowired
    private AlumnoActividadServicio alumnoActividadServicio;

    @GetMapping
    public List<Ent_AlumnoActividad> obtenerTodasLasAlumnoActividades() {
        return alumnoActividadServicio.obtenerTodasLasAlumnoActividades();
    }
}
