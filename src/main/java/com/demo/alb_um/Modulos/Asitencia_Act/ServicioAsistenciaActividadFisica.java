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

        // Obtener las asistencias asociadas a la lista
        List<Ent_AsistenciaActividadFisica> asistencias = asistenciaRepositorio.findByLista(lista);
    
        // Mapear asistencias por ID del alumno
        Map<Long, Ent_AsistenciaActividadFisica> asistenciaMap = asistencias.stream()
            .collect(Collectors.toMap(
                asistencia -> asistencia.getUsuarioAlumno().getIdUsuarioAlumno(),
                asistencia -> asistencia
            ));
    
        // Mapear las asistencias a los DTO de alumnos
        return alumnos.stream()
            .peek(alumno -> {
                Ent_AsistenciaActividadFisica asistencia = asistenciaMap.get(alumno.getIdUsuarioAlumno());
                if (asistencia != null) {
                    // Verificar estado de falta y asignar valores
                    alumno.setYaAsistio(asistencia.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.PRESENTE);
                    alumno.setEstadoFalta(asistencia.getEstadoFalta().name()); // Asignar estado de falta como texto
                    alumno.setFechaRegistro(asistencia.getFechaRegistro());
                } else {
                    alumno.setYaAsistio(false); // No hay asistencia registrada
                    alumno.setEstadoFalta("FALTA"); // Estado por defecto
                    alumno.setFechaRegistro(null); // No hay fecha de registro
                }
            })
            .collect(Collectors.toList());
    }
    
}
