package com.demo.alb_um.Login;

import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerInscripcionDTO;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;
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

    private final InscripcionTallerServicio inscripcionTallerServicio;


    @Autowired
    public Servicios_Controllers(InscripcionTallerServicio inscripcionTallerServicio) {
        this.inscripcionTallerServicio = inscripcionTallerServicio;
    }
    
    public String cargarVistaAdmin(String userName, UsuarioAdminServicio usuarioAdminServicio, Model model) {
        Optional<AdminDTO> adminOpt = usuarioAdminServicio.obtenerInformacionAdminPorUserName(userName);
        if (adminOpt.isPresent()) {
            AdminDTO admin = adminOpt.get();
            model.addAttribute("admin", admin);

            switch (admin.getServicio().getNombre()) {
                case "Antropometria":
                    List<CitaDTO> citasPendientes = usuarioAdminServicio.obtenerCitasPendientesPorServicio(
                        admin.getServicio().getIdServicio());
                    model.addAttribute("citasPendientes", citasPendientes);
                    break;
                
                case "Talleres":
                    try {
                        // Obtener y loguear los talleres del día
                        List<TallerDTO> talleresDelDia = inscripcionTallerServicio.obtenerTalleresDelDia();
                        System.out.println("Talleres encontrados para el día: " + talleresDelDia.size());
                        
                        // Imprimir cada taller para debug
                        talleresDelDia.forEach(taller -> {
                            System.out.println(String.format(
                                "Taller: %s, Fecha: %s, Hora: %s, Estado: %s",
                                taller.getNombre(),
                                taller.getFecha(),
                                taller.getHora(),
                                taller.getEstado()
                            ));
                        });

                        // Filtrar talleres en curso
                        List<TallerDTO> talleresEnCurso = talleresDelDia.stream()
                            .filter(t -> t.getEstado() == EstadoTaller.EN_CURSO)
                            .collect(Collectors.toList());
                        
                        model.addAttribute("talleresDelDia", talleresDelDia);
                        model.addAttribute("talleresEnCurso", talleresEnCurso);
                        model.addAttribute("horaActual", LocalDateTime.now());
                        
                    } catch (Exception e) {
                        System.err.println("Error al cargar talleres: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                
                default:
                    break;
            }

            return "admin";
        }
        return "error";
    }
}
