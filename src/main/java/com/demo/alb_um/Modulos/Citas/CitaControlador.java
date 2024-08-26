package com.demo.alb_um.Modulos.Citas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaControlador {

    @Autowired
    private CitaServicio citaServicio;

    @GetMapping
    public List<Ent_Cita> obtenerTodasLasCitas() {
        return citaServicio.obtenerTodasLasCitas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ent_Cita> obtenerCitaPorId(@PathVariable Long id) {
        return citaServicio.obtenerCitaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ent_Cita crearCita(@RequestBody Ent_Cita cita) {
        return citaServicio.guardarCita(cita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaServicio.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}
