package com.demo.alb_um.Modulos.Asitencia_Act;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaActividadFisicaControlador {

    @Autowired
    private ServicioAsistenciaActividadFisica asistenciaActividadFisicaServicio;

    @GetMapping
    public List<Ent_AsistenciaActividadFisica> obtenerTodasLasAsistencias() {
        return asistenciaActividadFisicaServicio.obtenerTodasLasAsistencias();
    }

    @PostMapping
    public Ent_AsistenciaActividadFisica crearAsistencia(@RequestBody Ent_AsistenciaActividadFisica asistencia) {
        return asistenciaActividadFisicaServicio.guardarAsistencia(asistencia);
    }

    @GetMapping("/{id}")
    public Ent_AsistenciaActividadFisica obtenerAsistenciaPorId(@PathVariable Long id) {
        return asistenciaActividadFisicaServicio.obtenerAsistenciaPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarAsistencia(@PathVariable Long id) {
        asistenciaActividadFisicaServicio.eliminarAsistencia(id);
    }
}
