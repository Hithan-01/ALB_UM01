package com.demo.alb_um.Modulos.Inscripcion_Taller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones_taller")
public class InscripcionTallerControlador {

    @Autowired
    private InscripcionTallerServicio inscripcionTallerServicio;

    @GetMapping
    public List<Ent_InscripcionTaller> obtenerTodasLasInscripciones() {
        return inscripcionTallerServicio.obtenerTodasLasInscripciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ent_InscripcionTaller> obtenerInscripcionPorId(@PathVariable Long id) {
        return inscripcionTallerServicio.obtenerInscripcionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ent_InscripcionTaller crearInscripcion(@RequestBody Ent_InscripcionTaller inscripcionTaller) {
        return inscripcionTallerServicio.guardarInscripcion(inscripcionTaller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionTallerServicio.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
