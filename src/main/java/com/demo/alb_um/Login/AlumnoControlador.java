package com.demo.alb_um.Login;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import java.util.Set;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioRepositorio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.HorarioServicioDTO;
import com.demo.alb_um.DTOs.TallerDTO;

@Controller
@RequestMapping("/alumno")
public class AlumnoControlador {

    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final HorarioServicioServicio horarioServicio;
    private final HorarioServicioRepositorio horarioServicioRepositorio;
    private final InscripcionTallerServicio inscripcionTallerServicio; // Nuevo servicio

    @Autowired
    public AlumnoControlador(UsuarioAlumnoServicio usuarioAlumnoServicio, 
                             CitaServicio citaServicio,
                             HorarioServicioServicio horarioServicio, 
                             HorarioServicioRepositorio horarioServicioRepositorio,
                             InscripcionTallerServicio inscripcionTallerServicio) { // Inyecta el nuevo servicio
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.citaServicio = citaServicio;
        this.horarioServicio = horarioServicio;
        this.horarioServicioRepositorio = horarioServicioRepositorio;
        this.inscripcionTallerServicio = inscripcionTallerServicio; // Asigna el servicio
    }

    @GetMapping("/servicios")
    public String mostrarServicios() {
        return "servicios"; // Vista de servicios generales
    } 

    @GetMapping("/talleres")
public String mostrarTalleres(Model model) {
    List<TallerDTO> talleresDisponibles = inscripcionTallerServicio.listarTalleresDisponibles();
    model.addAttribute("talleres", talleresDisponibles);
    return "talleres";
}

@PostMapping("/talleres/inscribir")
public String inscribirTaller(@RequestParam("idTaller") Long idTaller, Principal principal, Model model) {
    // Obtener el alumno autenticado basado en el username
    String username = principal.getName();
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(username);

    if (!alumnoOpt.isPresent()) {
        model.addAttribute("error", "No se encontró al alumno autenticado.");
        return "error";
    }

    AlumnoDTO alumno = alumnoOpt.get();
    Long idAlumno = alumno.getIdUsuarioAlumno();

    // Inscribir al alumno en el taller
    String mensaje = inscripcionTallerServicio.inscribirAlumno(idTaller, idAlumno);
    model.addAttribute("mensaje", mensaje);
    
    // Redirigir a la página de talleres con un mensaje de éxito o error
    return "redirect:/alumno/talleres";
}


    
    @GetMapping("/horariosDisponibles")
    public String verHorariosDisponibles(Model model, Principal principal) {
        // Obtener el nombre de usuario del alumno autenticado
        String username = principal.getName();
    
        // Buscar al alumno por su username
        Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(username);
    
        if (alumnoOpt.isPresent()) {
            AlumnoDTO alumno = alumnoOpt.get();
    
            // Añadir el alumno al modelo
            model.addAttribute("alumno", alumno);
    
            // Obtener los horarios disponibles para la facultad del alumno
            List<HorarioServicioDTO> horariosDisponibles = horarioServicio.obtenerHorariosDisponiblesPorFacultad(alumno.getFacultad());
    
            if (!horariosDisponibles.isEmpty()) {
                // Agrupar los horarios por día
                Map<LocalDate, List<HorarioServicioDTO>> horariosPorDia = horariosDisponibles.stream()
                    .collect(Collectors.groupingBy(HorarioServicioDTO::getDiaSemana)); // Correctamente agrupamos por diaSemana
                
                // Añadir los horarios agrupados al modelo
                model.addAttribute("horariosPorDia", horariosPorDia);
                return "horariosDisponibles";
            } else {
                // Si no hay horarios disponibles, mostrar un mensaje de error
                model.addAttribute("mensaje", "No hay horarios disponibles para tu facultad en este momento.");
                return "horariosDisponibles";
            }
        } else {
            // Si no se encuentra el alumno, añadir un mensaje de error
            model.addAttribute("error", "No se encontró al alumno en el sistema.");
            return "error";
        }
    }
    

   @PostMapping("/agendarCita")
public String agendarCita(@RequestParam("horarioId") Long horarioId, Model model, Principal principal) {
    // Obtener el alumno autenticado basado en el username del Principal
    String usernameAlumno = principal.getName();
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(usernameAlumno);

    if (!alumnoOpt.isPresent()) {
        model.addAttribute("error", "No se encontró al alumno autenticado.");
        return "error";
    }

    AlumnoDTO alumno = alumnoOpt.get();

    // Obtener el horario, el servicio y el admin asociado en una sola consulta
    Optional<Ent_HorarioServicio> horarioOpt = horarioServicioRepositorio.findHorarioConAdminPorId(horarioId);

    if (!horarioOpt.isPresent()) {
        model.addAttribute("error", "No se encontró el horario especificado.");
        return "error";
    }

    Ent_HorarioServicio horarioServicio = horarioOpt.get();
    
    // Obtener el primer administrador disponible del servicio
    Set<Ent_UsuarioAdmin> administradores = horarioServicio.getServicio().getUsuariosAdmin();
    if (administradores == null || administradores.isEmpty()) {
        model.addAttribute("error", "No se encontró un administrador vinculado al servicio de este horario.");
        return "error";
    }

    // Seleccionamos el primer administrador (suponiendo que hay uno)
    Ent_UsuarioAdmin admin = administradores.iterator().next();

    // Generar la cita para el alumno y vincularla con el admin obtenido desde el servicio
    citaServicio.generarCitaParaAntropometria(alumno.getIdUsuarioAlumno(), horarioId, admin);

    model.addAttribute("mensaje", "Cita de Antropometría agendada correctamente para: " + alumno.getNombreCompleto());
    return "resultadoCita";
}

}