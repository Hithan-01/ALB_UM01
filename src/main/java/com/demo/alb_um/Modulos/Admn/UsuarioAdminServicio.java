package com.demo.alb_um.Modulos.Admn;

import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;
import com.demo.alb_um.Modulos.Taller.TallerRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioAdminServicio {

    @Autowired
    private UsuarioAdminRepositorio usuarioAdminRepositorio;

    @Autowired
    private CitaRepositorio citaRepositorio;

    @Autowired TallerRepositorio tallerRepository;

    @Autowired
    private InscripcionTallerServicio inscripcionTallerServicio;

    /**
     * Obtiene la información completa del administrador, incluyendo su nombre, cargo, servicio y las citas asociadas.
     * @param userName El nombre de usuario del administrador.
     * @return Optional con AdminDTO si se encuentra el administrador, o Optional.empty() si no.
     */
    public Optional<AdminDTO> obtenerInformacionAdminPorUserName(String userName) {
        return usuarioAdminRepositorio.findByUsuario_UserName(userName)
                .map(this::convertirAdminAAdminDTO);  // Mapeamos directamente a AdminDTO si el admin está presente
    }

    /**
     * Convierte un Ent_UsuarioAdmin a un AdminDTO, con toda su información personal, de servicio y citas.
     * @param admin La entidad Ent_UsuarioAdmin a convertir.
     * @return El objeto AdminDTO con los detalles completos.
     */
    private AdminDTO convertirAdminAAdminDTO(Ent_UsuarioAdmin admin) {
        String nombreCompleto = admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido();
        String cargoServicio = admin.getCargoServicio();
        Ent_Servicio servicio = admin.getServicio();

        // Convertimos todas las citas asociadas a este admin
        List<CitaDTO> citas = admin.getCitas().stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());

        return new AdminDTO(nombreCompleto, cargoServicio, servicio, citas);
    }

    /**
     * Obtiene todas las citas pendientes asociadas a un servicio.
     * @param servicioId El ID del servicio.
     * @return Lista de CitaDTO con las citas pendientes.
     */
    public List<CitaDTO> obtenerCitasPendientesPorServicio(Long servicioId) {
        return citaRepositorio.findByUsuarioAdmin_Servicio_IdServicioAndVerificacionFalse(servicioId)
                .stream()
                .map(this::convertirACitaDTO)  // Reutilizamos la conversión de Cita a CitaDTO
                .collect(Collectors.toList());
    }

    /**
     * Convierte una Ent_Cita a un CitaDTO.
     * @param cita La entidad Ent_Cita a convertir.
     * @return El objeto CitaDTO con los detalles de la cita.
     */
    private CitaDTO convertirACitaDTO(Ent_Cita cita) {
        LocalDate diaSemana = null;
        LocalTime hora = null;

        // Verificamos si el horario de servicio es nulo para no lanzar errores de null pointer
        if (cita.getHorarioServicio() != null) {
            diaSemana = cita.getHorarioServicio().getDiaSemana();
            hora = cita.getHorarioServicio().getHora();
        }

        String nombreCompletoAlumno = cita.getUsuarioAlumno().getUsuario().getNombre() + " " + cita.getUsuarioAlumno().getUsuario().getApellido();

        return new CitaDTO(
                cita.getIdCita(),
                cita.getUsuarioAdmin().getServicio().getNombre(),
                diaSemana,
                hora,
                cita.getEstadoAsistencia(),
                cita.getVerificacion(),
                cita.getAutorizadoPor(),
                nombreCompletoAlumno
        );
    }

    public List<TallerDTO> obtenerTalleresFinalizadosHoy() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        Integer duracionMinima = 1; // O el valor mínimo de duración que quieras considerar
    
        return tallerRepository.findByFechaAndHoraBeforeAndDuracion(hoy, ahora, duracionMinima)
            .stream()
            .filter(taller -> {
                LocalTime horaFinTaller = taller.getHora().toLocalTime().plusMinutes(taller.getDuracion());
                return ahora.isAfter(horaFinTaller);
            })
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private TallerDTO convertToDTO(Ent_Taller taller) {
        TallerDTO dto = new TallerDTO(
            taller.getIdTaller(),
            taller.getNombre(),
            taller.getDescripcion(),
            taller.getFecha().toLocalDate(),
            taller.getHora().toLocalTime(),
            taller.getDuracion(),
            taller.getCuposDisponibles(),
            taller.getEstado(),
            taller.getTiempoTranscurrido()
        );
        
        // Establecer campos adicionales que no están en el constructor
        dto.setTallerLleno(taller.getCuposDisponibles() == 0);
        dto.setEstaInscrito(false);  // Este valor se debe actualizar en otro lugar según la lógica de tu aplicación
        
        return dto;
    }

    
    public List<TallerDTO> obtenerTalleresDelDia() {
        return inscripcionTallerServicio.obtenerTalleresDelDia();
    }

    public List<TallerDTO> obtenerTalleresEnCurso() {
    return tallerRepository.findByEstado(EstadoTaller.EN_CURSO)
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
}
}
