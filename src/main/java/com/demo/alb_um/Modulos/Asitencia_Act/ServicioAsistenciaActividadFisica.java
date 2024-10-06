package com.demo.alb_um.Modulos.Asitencia_Act;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicioAsistenciaActividadFisica {

    @Autowired
    private RepositorioAsistenciaActividadFisica asistenciaRepositorio;

    public List<AlumnoDTO> obtenerAlumnosConAsistencia(List<AlumnoDTO> alumnos, Entidad_Lista lista) {
    
        List<Ent_AsistenciaActividadFisica> asistencias = asistenciaRepositorio.findByLista(lista);

 
        Map<Long, Ent_AsistenciaActividadFisica> asistenciaMap = asistencias.stream()
            .collect(Collectors.toMap(asistencia -> asistencia.getUsuarioAlumno().getIdUsuarioAlumno(), asistencia -> asistencia));

        
        return alumnos.stream()
            .peek(alumno -> {
                Ent_AsistenciaActividadFisica asistencia = asistenciaMap.get(alumno.getIdUsuarioAlumno());
                if (asistencia != null) {
                    alumno.setYaAsistio(asistencia.isPresente());
                    alumno.setFechaRegistro(asistencia.getFechaRegistro());
                } else {
                    alumno.setYaAsistio(false);  // No hay asistencia registrada
                    alumno.setFechaRegistro(null); // No hay fecha de registro
                }
            })
            .collect(Collectors.toList());
    }
}
