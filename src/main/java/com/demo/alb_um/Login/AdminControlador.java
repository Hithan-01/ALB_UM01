package com.demo.alb_um.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.security.Principal;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminRepositorio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.DTOs.AlumnoDTO;

@Controller
@RequestMapping("/portal/admin")
public class AdminControlador {

    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final UsuarioAdminRepositorio usuarioAdminRepositorio;

    @Autowired
    public AdminControlador(UsuarioAlumnoServicio usuarioAlumnoServicio, 
                            CitaServicio citaServicio, 
                            UsuarioAdminRepositorio usuarioAdminRepositorio) {
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.citaServicio = citaServicio;
        this.usuarioAdminRepositorio = usuarioAdminRepositorio;
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
}
