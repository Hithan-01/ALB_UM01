package com.demo.alb_um.Modulos.Caminata;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.demo.alb_um.Modulos.Listas.Repositorio_lista;

@Service
public class CaminataServicio {


    @Autowired
    RepositorioAsistenciaActividadFisica asistenciaRepositorio;
    @Autowired
    UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;
    @Autowired
    CaminataRepositorio caminataRepositorio;
    @Autowired
    Repositorio_lista listaRepositorio;


    @Transactional
    public void registrarPasos(Long idLista, List<Long> idAlumnos, List<Integer> pasosSemanales) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSemana = ahora.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime finSemana = ahora.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
    
        // Obtener la lista asociada al ID
        Entidad_Lista lista = listaRepositorio.findById(idLista)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada con ID: " + idLista));
    
        for (int i = 0; i < idAlumnos.size(); i++) {
            Long idAlumno = idAlumnos.get(i);
            int pasos = pasosSemanales.get(i);
    
            // Buscar al alumno
            Entidad_Usuario_Alumno alumno = usuarioAlumnoRepositorio.findById(idAlumno)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    
            // Buscar la asistencia en la semana actual
            Optional<Ent_AsistenciaActividadFisica> asistenciaOpt =
                    asistenciaRepositorio.findByUsuarioAlumnoAndFechaRegistroBetween(alumno, inicioSemana, finSemana);
    
            // Si no existe, crear un nuevo registro de asistencia
            Ent_AsistenciaActividadFisica asistencia = asistenciaOpt.orElseGet(() -> {
                Ent_AsistenciaActividadFisica nuevaAsistencia = new Ent_AsistenciaActividadFisica();
                nuevaAsistencia.setUsuarioAlumno(alumno);
                nuevaAsistencia.setFechaRegistro(LocalDateTime.now());
                nuevaAsistencia.setEstadoFalta(Ent_AsistenciaActividadFisica.EstadoFalta.FALTA);
                nuevaAsistencia.setLista(lista);
                return asistenciaRepositorio.save(nuevaAsistencia);
            });
    
            // Asociar la lista si aún no tiene una
            if (asistencia.getLista() == null) {
                asistencia.setLista(lista);
                asistenciaRepositorio.save(asistencia);
            }
    
            // **Verificar si ya existe un registro en caminata para esta asistencia**
            Optional<Entidad_Caminata> caminataOpt = caminataRepositorio.findByAsistencia(asistencia);
    
            if (caminataOpt.isPresent()) {
                // Si ya existe, actualizar los pasos
                Entidad_Caminata caminataExistente = caminataOpt.get();
                caminataExistente.setPasosSemanales(pasos);
                caminataRepositorio.save(caminataExistente);
            } else {
                // Si no existe, crear un nuevo registro
                Entidad_Caminata nuevaCaminata = new Entidad_Caminata();
                nuevaCaminata.setPasosSemanales(pasos);
                nuevaCaminata.setSemanaRegistro(obtenerNumeroSemana(ahora.toLocalDate()));
                nuevaCaminata.setAsistencia(asistencia);
                caminataRepositorio.save(nuevaCaminata);
            }
    
            // Actualizar la asistencia si los pasos superan el mínimo
            if (pasos >= 70000) { // 10,000 pasos diarios
                asistencia.setEstadoFalta(Ent_AsistenciaActividadFisica.EstadoFalta.PRESENTE);
                asistenciaRepositorio.save(asistencia);
            }
        }
    }
    

public Map<Long, Integer> obtenerPasosRegistrados(List<Long> idAlumnos) {
    Map<Long, Integer> pasosRegistrados = new HashMap<>();

    for (Long idAlumno : idAlumnos) {
        // Buscar la asistencia del alumno en la semana actual
        Entidad_Usuario_Alumno alumno = usuarioAlumnoRepositorio.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSemana = ahora.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime finSemana = ahora.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);

        Optional<Ent_AsistenciaActividadFisica> asistenciaOpt =
                asistenciaRepositorio.findByUsuarioAlumnoAndFechaRegistroBetween(alumno, inicioSemana, finSemana);

        if (asistenciaOpt.isPresent()) {
            // Buscar si ya tiene pasos registrados
            Optional<Entidad_Caminata> caminataOpt = caminataRepositorio.findByAsistencia(asistenciaOpt.get());

            if (caminataOpt.isPresent()) {
                pasosRegistrados.put(idAlumno, caminataOpt.get().getPasosSemanales());
            }
        }
    }
    
    return pasosRegistrados;
}

    
public int obtenerNumeroSemana(LocalDate fecha) {
    return fecha.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
}

    
}
