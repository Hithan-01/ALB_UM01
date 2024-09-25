package com.demo.alb_um.Modulos.Actividad_Fisica;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class ActividadFisicaServicio {

    @Autowired
    private ActividadFisicaRepositorio actividadFisicaRepositorio;
    @Autowired
private RepositorioAsistenciaActividadFisica asistenciaRepositorio;

    public List<ActividadFisicaDTO> obtenerTodasLasActividadesFisicas() {
        return actividadFisicaRepositorio.findAll().stream()
                .map(this::convertirAActividadFisicaDTO)
                .collect(Collectors.toList());
    }

private ActividadFisicaDTO convertirAActividadFisicaDTO(Entidad_ActividadFisica actividadFisica) {
    List<AlumnoDTO> alumnos = actividadFisica.getAlumnoActividades().stream()
        .map((Ent_AlumnoActividad alumnoActividad) -> {
            Entidad_Usuario_Alumno usuarioAlumno = alumnoActividad.getUsuarioAlumno();

            // Filtrar las listas de la actividad para obtener una lista con la fecha correspondiente o algún otro criterio
            Optional<Entidad_Lista> listaOpt = actividadFisica.getListas().stream()
                .findFirst(); // Si tienes un criterio, aplícalo aquí. De lo contrario, toma la primera lista.

            if (listaOpt.isPresent()) {
                // Buscar la asistencia del alumno en la lista encontrada
                Optional<Ent_AsistenciaActividadFisica> asistencia = asistenciaRepositorio.findByListaAndUsuarioAlumno(listaOpt.get(), usuarioAlumno);
                return convertirAAlumnoDTO(usuarioAlumno, actividadFisica, asistencia.orElse(null));
            } else {
                return convertirAAlumnoDTO(usuarioAlumno, actividadFisica, null);  // Si no hay lista, retorna un DTO sin asistencia
            }
        })
        .collect(Collectors.toList());

    return new ActividadFisicaDTO(
        actividadFisica.getIdActividadFisica(),
        actividadFisica.getNombre(),
        actividadFisica.getGrupo(),
        actividadFisica.getDiaSemana(),
        actividadFisica.getHora(),
        alumnos
    );
}


    
private AlumnoDTO convertirAAlumnoDTO(Entidad_Usuario_Alumno usuarioAlumno, Entidad_ActividadFisica actividadFisica, Ent_AsistenciaActividadFisica asistencia) {
    Entidad_Usuario usuario = usuarioAlumno.getUsuario();
    String horario = actividadFisica.getDiaSemana() + " " + (actividadFisica.getHora() != null ? actividadFisica.getHora().toString() : "");

    // Verificar si ya asistió y establecer correctamente la fecha de registro
    boolean yaAsistio = asistencia != null && asistencia.isPresente();
    LocalDateTime fechaRegistro = asistencia != null ? asistencia.getFechaRegistro() : null;

    // Retornamos el DTO con todos los campos, incluyendo 'yaAsistio' y 'fechaRegistro'
    return new AlumnoDTO(
        usuarioAlumno.getIdUsuarioAlumno(),
        usuario.getNombre() + " " + usuario.getApellido(),
        actividadFisica.getNombre(),
        usuario.getEmail(),
        horario,
        yaAsistio,  // Ya asistió
        fechaRegistro  // Fecha de registro obtenida correctamente
    );
}

   
    
}



