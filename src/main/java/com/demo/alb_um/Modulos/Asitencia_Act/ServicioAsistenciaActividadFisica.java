package com.demo.alb_um.Modulos.Asitencia_Act;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioAsistenciaActividadFisica {

    @Autowired
    private RepositorioAsistenciaActividadFisica asistenciaActividadFisicaRepositorio;

    public List<Ent_AsistenciaActividadFisica> obtenerTodasLasAsistencias() {
        return asistenciaActividadFisicaRepositorio.findAll();
    }

    public Ent_AsistenciaActividadFisica guardarAsistencia(Ent_AsistenciaActividadFisica asistencia) {
        return asistenciaActividadFisicaRepositorio.save(asistencia);
    }

    public Ent_AsistenciaActividadFisica obtenerAsistenciaPorId(Long id) {
        return asistenciaActividadFisicaRepositorio.findById(id).orElse(null);
    }

    public void eliminarAsistencia(Long id) {
        asistenciaActividadFisicaRepositorio.deleteById(id);
    }
}
