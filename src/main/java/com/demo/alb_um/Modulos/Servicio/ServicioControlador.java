package com.demo.alb_um.Modulos.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioControlador {

    @Autowired
    private Servicio_Servicio servicioServicio;

    @GetMapping
    public List<Ent_Servicio> obtenerTodosLosServicios() {
        return servicioServicio.obtenerTodosLosServicios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ent_Servicio> obtenerServicioPorId(@PathVariable Long id) {
        return servicioServicio.obtenerServicioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ent_Servicio crearServicio(@RequestBody Ent_Servicio servicio) {
        return servicioServicio.guardarServicio(servicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioServicio.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
