package com.demo.alb_um.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminRepositorio;
// Importa tu servicio y DTO
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.DTOs.AlumnoDTO;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminControlador {

   private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citasServicio;
    private final UsuarioAdminRepositorio usuarioAdminRepositorio;

    @Autowired
    public AdminControlador(UsuarioAlumnoServicio usuarioAlumnoServicio, CitaServicio citaServicio, UsuarioAdminRepositorio usuarioAdminRepositorio) {
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.citasServicio = citaServicio;
        this.usuarioAdminRepositorio = usuarioAdminRepositorio;
    }

    @GetMapping("/buscarAlumno")
    public String buscarAlumno(@RequestParam("search") String search, Model model) {
        Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(search);
        if (alumnoOpt.isPresent()) {
            AlumnoDTO alumno = alumnoOpt.get();
            model.addAttribute("alumno", alumno);
            return "verAlumno"; // Redirige a la vista donde se muestran los datos del alumno
        } else {
            model.addAttribute("error", "Alumno no encontrado");
            return "buscarAlumno"; // Si no se encuentra el alumno, regresa a la misma página con un mensaje de error
        }
    }

    
    @PostMapping("/generarAsistencia")
public String generarAsistencia(@RequestParam("alumnoId") Long alumnoId, 
                                @RequestParam("estadoAsistencia") String estadoAsistencia, 
                                Model model, Principal principal) {
    // Obtener información del alumno basado en su ID
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorId(alumnoId);
    
    if (alumnoOpt.isPresent()) {
        AlumnoDTO alumno = alumnoOpt.get();
        
        // Obtener información del administrador actual usando el principal (nombre de usuario)
        String usernameAdmin = principal.getName();
        Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(usernameAdmin);
        
        if (adminOpt.isPresent()) {
            Ent_UsuarioAdmin admin = adminOpt.get();
            
            // Generar la cita para el alumno en el servicio del administrador
            citasServicio.generarCita(admin, alumno.getIdUsuarioAlumno(), estadoAsistencia);
            
            model.addAttribute("mensaje", "Asistencia generada correctamente para el alumno: " + alumno.getNombreCompleto());
        } else {
            model.addAttribute("error", "No se encontró el administrador.");
        }
    } else {
        model.addAttribute("error", "Alumno no encontrado.");
    }
    return "resultadoAsistencia"; // Página que muestra el resultado
}

    


}
