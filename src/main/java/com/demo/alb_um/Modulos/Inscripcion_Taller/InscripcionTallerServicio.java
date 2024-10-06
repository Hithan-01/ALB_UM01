package com.demo.alb_um.Modulos.Inscripcion_Taller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.demo.alb_um.DTOs.TallerDTO;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.demo.alb_um.Modulos.Taller.TallerRepositorio;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;

@Service
public class InscripcionTallerServicio {

    @Autowired
    private InscripcionTallerRepositorio RepositorioInscripcionTaller;

    @Autowired
    private TallerRepositorio tallerRepository;

    @Autowired
    private UsuarioAlumnoRepositorio AlumnoRepositorio;

   // Método para verificar usando el username (String)
public boolean estaInscritoEnTaller(String username, Long idTaller) {
    return RepositorioInscripcionTaller.existsByUsuarioAlumno_Usuario_UserNameAndTaller_IdTaller(username, idTaller);
}

// Método para verificar usando el idAlumno (Long)
public boolean estaInscritoEnTaller(Long idAlumno, Long idTaller) {
    return RepositorioInscripcionTaller.existsByUsuarioAlumno_IdUsuarioAlumnoAndTaller_IdTaller(idAlumno, idTaller);
}
    

    public List<Long> obtenerTalleresInscritosIds(String userName) {
        List<Ent_InscripcionTaller> talleresInscritos = RepositorioInscripcionTaller.findByUsuarioAlumno_Usuario_UserName(userName);
        return talleresInscritos.stream()
                .map(taller -> taller.getTaller().getIdTaller())
                .collect(Collectors.toList());
    }
    
    

// Método para listar todos los talleres (incluyendo los que no tienen cupos disponibles)
public List<TallerDTO> listarTalleresDisponibles() {
    List<Ent_Taller> talleres = tallerRepository.findAll();  // Obtener todos los talleres

    // Convertimos cada Ent_Taller a TallerDTO
    return talleres.stream()
        .map(taller -> {
            TallerDTO dto = new TallerDTO(
                taller.getIdTaller(),
                taller.getNombre(),
                taller.getDescripcion(),
                taller.getFecha().toLocalDate(),
                taller.getHora().toLocalTime(),
                taller.getCuposDisponibles());
                
            // Si no hay cupos disponibles, marcamos el taller como lleno
            if (taller.getCuposDisponibles() == 0) {
                dto.setTallerLleno(true);  // Aquí asumimos que agregas un setter para este campo
            }
            
            return dto;
        })
        .collect(Collectors.toList());
}

    

    // Método para inscribir un alumno a un taller (sin cambios)
    public String inscribirAlumno(Long idTaller, Long idAlumno) {
        Optional<Ent_Taller> tallerOptional = tallerRepository.findById(idTaller);
        Optional<Entidad_Usuario_Alumno> alumnoOptional = AlumnoRepositorio.findById(idAlumno);

        if (tallerOptional.isPresent() && alumnoOptional.isPresent()) {
            Ent_Taller taller = tallerOptional.get();
            Entidad_Usuario_Alumno alumno = alumnoOptional.get();

            // Verificar que haya cupos disponibles
            if (taller.getCuposDisponibles() > 0) {
                Ent_InscripcionTaller inscripcion = new Ent_InscripcionTaller();
                inscripcion.setTaller(taller);
                inscripcion.setUsuarioAlumno(alumno);
                inscripcion.setEstadoAsistencia("pendiente");
                inscripcion.setVerificacion(false);

                // Guardar la inscripción
                RepositorioInscripcionTaller.save(inscripcion);

                // Actualizar los cupos disponibles
                taller.setCuposDisponibles(taller.getCuposDisponibles() - 1);
                tallerRepository.save(taller);

                return "Inscripción exitosa";
            } else {
                return "Cupos agotados";
            }
        } else {
            return "Taller o alumno no encontrados";
        }
    }
}
