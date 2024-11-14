package com.demo.alb_um.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.security.Principal;
import java.time.LocalTime;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminRepositorio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerRepositorio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.DTOs.AlumnoDTO;


@Controller
@RequestMapping("/portal/admin")
public class AdminControlador {

    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final UsuarioAdminRepositorio usuarioAdminRepositorio;
    private final InscripcionTallerServicio inscripcionTallerServicio;

    private final InscripcionTallerRepositorio inscripcionTallerRepositorio;
    @Autowired
    public AdminControlador(UsuarioAlumnoServicio usuarioAlumnoServicio, 
                            CitaServicio citaServicio, 
                            UsuarioAdminRepositorio usuarioAdminRepositorio, 
                            InscripcionTallerServicio inscripcionTallerServicio, 
                            InscripcionTallerRepositorio inscripcionTallerRepositorio) {
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.citaServicio = citaServicio;
        this.usuarioAdminRepositorio = usuarioAdminRepositorio;
        this.inscripcionTallerServicio = inscripcionTallerServicio;
        this.inscripcionTallerRepositorio = inscripcionTallerRepositorio;
    }

    // Buscar Alumno - Para administradores que no sean de Antropometría
    @GetMapping("/general/buscarAlumno")
    public String buscarAlumno(@RequestParam("search") String search, Model model, Principal principal) {
        Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
        if (adminOpt.isPresent() && !esAdminAntropometria(adminOpt.get())) {
            Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(search);
            if (alumnoOpt.isPresent()) {
                model.addAttribute("alumno", alumnoOpt.get());
                return "verAlumno"; // Página de visualización de datos del alumno
            } else {
                model.addAttribute("error", "Alumno no encontrado");
            }
        } else {
            model.addAttribute("error", "No tienes permiso para realizar esta acción.");
        }
        return "error";
    }

    @PostMapping("/general/generarAsistencia")
    public String generarAsistencia(@RequestParam("alumnoId") Long alumnoId, 
                                    @RequestParam("estadoAsistencia") String estadoAsistencia, 
                                    Model model, Principal principal) {
        Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
        if (adminOpt.isPresent() && !esAdminAntropometria(adminOpt.get())) {
            Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorId(alumnoId);
            if (alumnoOpt.isPresent()) {
                AlumnoDTO alumno = alumnoOpt.get();
                citaServicio.generarCita(adminOpt.get(), alumno.getIdUsuarioAlumno(), estadoAsistencia);
                model.addAttribute("mensaje", "Asistencia generada correctamente.");
                return "resultadoAsistencia";
            } else {
                model.addAttribute("error", "Alumno no encontrado.");
            }
        } else {
            model.addAttribute("error", "No tienes permiso para realizar esta acción.");
        }
        return "error";
    }

    // Validar Cita - Exclusivo para administradores de Antropometría
    @PostMapping("/antropometria/validarCita")
    public String validarCita(@RequestParam("citaId") Long citaId, RedirectAttributes redirectAttributes, Principal principal) {
        Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());

        if (adminOpt.isPresent() && esAdminAntropometria(adminOpt.get())) {
            Ent_UsuarioAdmin admin = adminOpt.get();

            try {
                citaServicio.validarCita(citaId, admin);
                redirectAttributes.addFlashAttribute("mensajeExito", "Cita validada correctamente.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al validar la cita.");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para realizar esta acción.");
        }
        
        return "redirect:/portal/inicio";
    }

    // Método compartido para verificar si un admin es de Antropometría
    private boolean esAdminAntropometria(Ent_UsuarioAdmin admin) {
        return "Antropometria".equals(admin.getServicio().getNombre());
    }

    @PostMapping("/taller/{id}/detener")  // Cambio aquí para coincidir con la ruta de la vista
    public String detenerTaller(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            inscripcionTallerServicio.finalizarTaller(id);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "Taller finalizado. Puede proceder a registrar hora de salida.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al finalizar el taller: " + e.getMessage());
        }
        return "redirect:/portal/inicio";  // También cambié el redirect para que vaya a la página principal
    }

    @GetMapping("/{id}/pasarLista")
    public String mostrarPaseLista(@PathVariable Long id, Model model) {
        // Obtener la lista de inscripciones para este taller
        List<Ent_InscripcionTaller> inscripciones = 
            inscripcionTallerServicio.obtenerInscripcionesPorTaller(id);
        
        model.addAttribute("tallerId", id);
        model.addAttribute("inscripciones", inscripciones);
        return "pasarListaTaller";
    }

    @PostMapping("/taller/{id}/registrarAsistencia")
    public String registrarAsistencia(@PathVariable Long id, 
                                    @RequestParam List<Long> asistentes, 
                                    RedirectAttributes redirectAttributes) {
        try {
            inscripcionTallerServicio.registrarAsistenciaTaller(id, asistentes);
            redirectAttributes.addFlashAttribute("mensaje", "Asistencia registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la asistencia");
        }
        return "redirect:/portal/admin";
    }

     @PostMapping("/{id}/registrarTag")
    @ResponseBody
    public Map<String, Object> registrarTag(
            @PathVariable Long id,
            @RequestParam String tagCredencial) {
        return inscripcionTallerServicio.registrarAsistenciaPorTag(id, tagCredencial);
    }
    
    @PostMapping("/taller/{id}/registrarLlegada")
    @ResponseBody
    public Map<String, Object> registrarLlegada(
            @PathVariable("id") Long tallerId,
            @RequestParam("identificador") String identificador) {
        
        System.out.println("Recibiendo solicitud para taller: " + tallerId + " con identificador: " + identificador);
        
        try {
            return inscripcionTallerServicio.registrarHoraLlegada(tallerId, identificador);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Error al registrar llegada: " + e.getMessage());
            e.printStackTrace();
            return response;
        }
    }

    @PostMapping("/{id}/registrarSalida")
@ResponseBody
public Map<String, Object> registrarSalida(
        @PathVariable Long id,
        @RequestParam String identificador) {
    Map<String, Object> response = new HashMap<>();

    try {
        Optional<Ent_InscripcionTaller> inscripcionOpt = 
            inscripcionTallerRepositorio.findByTallerAndIdentificador(id, identificador);

        if (!inscripcionOpt.isPresent()) {
            response.put("error", true);
            response.put("message", "Alumno no inscrito en este taller.");
            return response;
        }

        Ent_InscripcionTaller inscripcion = inscripcionOpt.get();

        // Verificar si tiene hora de llegada válida
        if (inscripcion.getHoraLlegada() == null) {
            response.put("error", true);
            response.put("message", "No se ha registrado una hora de llegada.");
            return response;
        }

        // Verificar si la hora de llegada fue válida
        if ("LLEGADA_INVALIDA".equals(inscripcion.getEstadoAsistencia())) {
            response.put("error", true);
            response.put("message", "La hora de llegada no fue válida.");
            return response;
        }

        // Registrar la hora de salida y actualizar el estado de asistencia
        inscripcion.setHoraSalida(LocalTime.now());
        inscripcion.setEstadoAsistencia("ASISTIO");
        inscripcion.setVerificacion(true);

        // Guardar la inscripción
        inscripcionTallerRepositorio.save(inscripcion);
        response.put("error", false);
        response.put("message", "Asistencia registrada correctamente.");

    } catch (Exception e) {
        response.put("error", true);
        response.put("message", "Error al registrar la salida: " + e.getMessage());
        e.printStackTrace();
    }

    return response;
}

   

     // Manejador de errores para este controlador
    @ExceptionHandler(Exception.class)
    public String manejarError(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/portal/admin/taller/listado";
    }
}
