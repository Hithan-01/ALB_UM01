package com.demo.alb_um.Login;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;
import java.util.Set;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Antropometria.AntropometriaServicio;
import com.demo.alb_um.Modulos.Antropometria.Ent_Antro;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Horario_servicio.Ent_HorarioServicio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioRepositorio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.CambioContrasenaDTO;
import com.demo.alb_um.DTOs.HorarioServicioDTO;
import com.demo.alb_um.DTOs.TallerDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/alumno")
public class AlumnoControlador {

    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final HorarioServicioServicio horarioServicio;
    private final HorarioServicioRepositorio horarioServicioRepositorio;
    private final InscripcionTallerServicio inscripcionTallerServicio; // Nuevo servicio
    private final CitaRepositorio citaRepositorio;
    private final AntropometriaServicio antropometriaServicio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;


    @GetMapping("/servicios")
public String mostrarServicios(Model model, Principal principal) {
    // Obtener el username del alumno autenticado
    String usernameAlumno = principal.getName();

    // Verificar si el alumno ya tiene una cita para Antropometría
    List<Ent_Cita> citasAntropometria = citaRepositorio.findByUsuarioAlumno_Usuario_UserNameAndVerificacionFalse(usernameAlumno);
    
    // Si ya tiene una cita para Antropometría, bloqueamos el botón
    boolean tieneCitaAntropometria = !citasAntropometria.isEmpty();
    
    // Pasamos esta información al modelo para la vista
    model.addAttribute("tieneCitaAntropometria", tieneCitaAntropometria);
    
    return "/Vistas_Alumno/Ver_Servicios"; // Vista de servicios generales
}


@GetMapping("/talleres")
public String mostrarTalleres(Model model, Principal principal) {
    String username = principal.getName();
    
    // Obtener la lista de talleres disponibles (ahora incluye los talleres llenos)
    List<TallerDTO> talleresDisponibles = inscripcionTallerServicio.listarTalleresDisponibles();
    
    // Para cada taller, verificar si el alumno ya está inscrito
    talleresDisponibles.forEach(taller -> {
        boolean estaInscrito = inscripcionTallerServicio.estaInscritoEnTaller(username, taller.getIdTaller());
        taller.setEstaInscrito(estaInscrito);  // Marcar si está inscrito
    });

    model.addAttribute("talleres", talleresDisponibles);
    return "/Vistas_Alumno/Ver_Talleres";
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

    // Verificar si el alumno ya está inscrito en este taller
    if (inscripcionTallerServicio.estaInscritoEnTaller(idAlumno, idTaller)) {
        // Si ya está inscrito, mostrar un mensaje de error
        model.addAttribute("error", "Ya estás inscrito en este taller.");
        List<TallerDTO> talleresDisponibles = inscripcionTallerServicio.listarTalleresDisponibles();
        model.addAttribute("talleres", talleresDisponibles);
        return "/Vistas_Alumno/Ver_Talleres";  // Regresar a la lista de talleres
    }
    

    // Si no está inscrito, proceder con la inscripción
    inscripcionTallerServicio.inscribirAlumno(idTaller, idAlumno);
    model.addAttribute("mensaje", "Te has inscrito exitosamente en el taller.");

    // Redirigir a la página de talleres con un mensaje de éxito
    return "redirect:/alumno/talleres";  // Redirigir para evitar doble submit
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

@GetMapping("/antropometria/{id}/datos")
public String verDatosAntropometricos(@PathVariable Long id, Model model) {
    List<Ent_Antro> datosAntroList = antropometriaServicio.obtenerDatosPorAlumno(id);

    if (datosAntroList != null && !datosAntroList.isEmpty()) {
        // Obtener el último registro (asumiendo que están ordenados por fecha de creación)
        Ent_Antro ultimoDato = datosAntroList.get(datosAntroList.size() - 1);
        model.addAttribute("datosAntro", ultimoDato);
        return "verDatosAntropometricos";
    } else {
        model.addAttribute("mensajeError", "No hay datos antropométricos disponibles para este alumno.");
        return "error";
    }
}

@GetMapping("/cambiar-contrasena")
    public String mostrarFormularioCambioContrasena(Model model) {
        model.addAttribute("cambioContrasenaDTO", new CambioContrasenaDTO());
        return "Vistas_Alumno/Cambio_Contrasena";
    }

    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(@ModelAttribute CambioContrasenaDTO cambioContrasenaDTO,
                                    RedirectAttributes redirectAttributes,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    HttpServletRequest request) {
    
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
            return "redirect:/login";
        }
    
        String username = userDetails.getUsername();
        Entidad_Usuario usuario = usuarioRepositorio.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        // Validar la nueva contraseña
        if (cambioContrasenaDTO.getNuevaContrasena().equals(username)) {
            redirectAttributes.addFlashAttribute("error", "No puedes usar tu matrícula como contraseña.");
            return "redirect:/alumno/cambiar-contrasena";
        }
    
        if (!cambioContrasenaDTO.getNuevaContrasena().equals(cambioContrasenaDTO.getConfirmarContrasena())) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "redirect:/alumno/cambiar-contrasena";
        }
    
        // Actualizar la contraseña
        usuario.setContrasena(passwordEncoder.encode(cambioContrasenaDTO.getNuevaContrasena()));
        usuarioRepositorio.save(usuario);
    
        // Cerrar sesión
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    
        redirectAttributes.addFlashAttribute("success", "Contraseña cambiada exitosamente. Inicia sesión nuevamente.");
        return "redirect:/login";
    }
    

@GetMapping("/ver-detalles-asistencias")
public String verDetallesAsistenciasAlumno(Principal principal, Model model) {
    // Obtener el usuario actual a partir del username
    Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findByUsuario_UserName(principal.getName());

    if (alumnoOpt.isPresent()) {
        Entidad_Usuario_Alumno alumno = alumnoOpt.get();

        // Obtener todas sus asistencias
        List<Map<String, Object>> asistencias = alumno.getAsistencias().stream()
            .map(asistencia -> {
                Map<String, Object> asistenciaInfo = new HashMap<>();
                asistenciaInfo.put("fechaRegistro", asistencia.getFechaRegistro());
                asistenciaInfo.put("estadoFalta", asistencia.getEstadoFalta() != null ? asistencia.getEstadoFalta().name() : "Sin estado");
                asistenciaInfo.put("actividad", asistencia.getLista().getActividadFisica().getNombre());
                asistenciaInfo.put("diaSemana", asistencia.getLista().getActividadFisica().getDiaSemana());
                asistenciaInfo.put("hora", asistencia.getLista().getActividadFisica().getHora());
                return asistenciaInfo;
            })
            .collect(Collectors.toList());

        model.addAttribute("alumno", alumno);
        model.addAttribute("asistencias", asistencias);

        return "/Vistas_Alumno/Detalle_Asistencias";
    } else {
        model.addAttribute("error", "No se encontró al alumno.");
        return "/Vistas_Alumno/Error";
    }
}

}
