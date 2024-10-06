package com.demo.alb_um.Login;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerInscripcionDTO;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminServicio;

@Service
public class Servicios_Controllers {

    public String cargarVistaCoach(String userName, CoachActividadServicio coachActividadServicio, Model model) {
        Optional<CoachDTO> coachOpt = coachActividadServicio.obtenerCoachPorUserName(userName);
        if (coachOpt.isPresent()) {
            model.addAttribute("coach", coachOpt.get());
            return "coach"; 
        }
        return "error"; 
    }

    public String cargarVistaAlumno(String userName, UsuarioAlumnoServicio usuarioAlumnoServicio, Model model) {
        Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.obtenerInformacionAlumnoPorUserName(userName);
        if (alumnoOpt.isPresent()) {
            AlumnoDTO alumno = alumnoOpt.get();
            
            // Obtener citas y talleres pendientes
            List<CitaDTO> citasPendientes = usuarioAlumnoServicio.obtenerCitasPendientes(userName);
            List<TallerInscripcionDTO> talleresPendientes = usuarioAlumnoServicio.obtenerTalleresPendientes(userName);

            // Obtener citas y talleres confirmados
            List<CitaDTO> citasConfirmadas = usuarioAlumnoServicio.obtenerCitasConfirmadas(userName);
            List<TallerInscripcionDTO> talleresConfirmados = usuarioAlumnoServicio.obtenerTalleresConfirmados(userName);

            // Añadir las listas al modelo
            model.addAttribute("alumno", alumno);
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("talleresPendientes", talleresPendientes);
            model.addAttribute("citasConfirmadas", citasConfirmadas);
            model.addAttribute("talleresConfirmados", talleresConfirmados);

            // Calcular si no hay pendientes
            boolean noPendientes = talleresPendientes.isEmpty() && citasPendientes.isEmpty();
            model.addAttribute("noPendientes", noPendientes);

            return "alumno"; 
        }
        return "error"; 
    }

    public String cargarVistaAdmin(String userName, UsuarioAdminServicio usuarioAdminServicio, Model model) {
        Optional<AdminDTO> adminOpt = usuarioAdminServicio.obtenerInformacionAdminPorUserName(userName);
        if (adminOpt.isPresent()) {
            AdminDTO admin = adminOpt.get();
            model.addAttribute("admin", admin);

            // Verificar si el servicio del admin es Antropometría
            if ("Antropometria".equals(admin.getServicio().getNombre())) {
                List<CitaDTO> citasPendientes = usuarioAdminServicio.obtenerCitasPendientesPorServicio(admin.getServicio().getIdServicio());
                model.addAttribute("citasPendientes", citasPendientes);
            }

            return "admin"; 
        }
        return "error"; 
    }
}
