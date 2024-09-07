package com.demo.alb_um.Modulos.Asitencia_Act;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.alb_um.DTOs.AlumnoDTO;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;

import java.util.List;
import java.util.Optional;

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

    @Autowired
private RepositorioAsistenciaActividadFisica asistenciaRepositorio; 


public List<AlumnoDTO> obtenerAlumnosConAsistencia(List<AlumnoDTO> alumnos, Entidad_Lista lista) {
    for (AlumnoDTO alumno : alumnos) {
        Entidad_Usuario_Alumno usuarioAlumno = new Entidad_Usuario_Alumno();
        usuarioAlumno.setIdUsuarioAlumno(alumno.getIdUsuarioAlumno());
        
        Optional<Ent_AsistenciaActividadFisica> asistencia = asistenciaRepositorio.findByListaAndUsuarioAlumno(lista, usuarioAlumno);
        
        // Verificar si ya existe la asistencia y marcar 'yaAsistio' basado en el campo 'presente'
        if (asistencia.isPresent()) {
            Ent_AsistenciaActividadFisica asistenciaExistente = asistencia.get();
            alumno.setYaAsistio(asistenciaExistente.isPresente());
        } else {
            alumno.setYaAsistio(false);  // Si no hay asistencia registrada, permitir marcar
        }
    }
    return alumnos;
}



 
}
