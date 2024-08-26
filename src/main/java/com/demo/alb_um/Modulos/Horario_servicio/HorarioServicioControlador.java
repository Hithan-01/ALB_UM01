package com.demo.alb_um.Modulos.Horario_servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios_servicio")
public class HorarioServicioControlador {

    @Autowired
    private HorarioServicioServicio horarioServicioServicio;

    @GetMapping
    public List<Ent_HorarioServicio> obtenerTodosLosHorariosServicio() {
        return horarioServicioServicio.obtenerTodosLosHorariosServicio();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ent_HorarioServicio> obtenerHorarioServicioPorId(@PathVariable Long id) {
        return horarioServicioServicio.obtenerHorarioServicioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ent_HorarioServicio crearHorarioServicio(@RequestBody Ent_HorarioServicio horarioServicio) {
        return horarioServicioServicio.guardarHorarioServicio(horarioServicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorarioServicio(@PathVariable Long id) {
        horarioServicioServicio.eliminarHorarioServicio(id);
        return ResponseEntity.noContent().build();
    }
}
