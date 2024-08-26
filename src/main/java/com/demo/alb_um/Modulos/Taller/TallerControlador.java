package com.demo.alb_um.Modulos.Taller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talleres")
public class TallerControlador {

    @Autowired
    private TallerServicio tallerServicio;

    @GetMapping
    public List<Ent_Taller> obtenerTodosLosTalleres() {
        return tallerServicio.obtenerTodosLosTalleres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ent_Taller> obtenerTallerPorId(@PathVariable Long id) {
        return tallerServicio.obtenerTallerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ent_Taller crearTaller(@RequestBody Ent_Taller taller) {
        return tallerServicio.guardarTaller(taller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTaller(@PathVariable Long id) {
        tallerServicio.eliminarTaller(id);
        return ResponseEntity.noContent().build();
    }
}
