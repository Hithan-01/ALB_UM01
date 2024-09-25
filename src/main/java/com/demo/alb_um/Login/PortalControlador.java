package com.demo.alb_um.Login;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.TallerDTO;

import com.demo.alb_um.Modulos.Admn.UsuarioAdminServicio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Asitencia_Act.ServicioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.demo.alb_um.Modulos.Listas.Servicio_lista;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/portal")
public class PortalControlador {

    private final CoachActividadServicio coachActividadServicio;
    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final UsuarioAdminServicio usuarioAdminServicio;
    private final Servicio_lista listaServicio;
    private final ServicioAsistenciaActividadFisica asistenciaActividadFisicaServicio;


public PortalControlador(CoachActividadServicio coachActividadServicio, 
                         UsuarioAlumnoServicio usuarioAlumnoServicio, 
                         UsuarioAdminServicio usuarioAdminServicio, 
                         Servicio_lista listaServicio,
                         ServicioAsistenciaActividadFisica asistenciaActividadFisicaServicio) {
    this.coachActividadServicio = coachActividadServicio;
    this.usuarioAlumnoServicio = usuarioAlumnoServicio;
    this.usuarioAdminServicio = usuarioAdminServicio;
    this.listaServicio = listaServicio; // Inyectamos el servicio lista
    this.asistenciaActividadFisicaServicio = asistenciaActividadFisicaServicio; // Inyectamos el servicio de asistencia
}


    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();


    // Calcular el progreso
    int progreso = usuarioAlumnoServicio.calcularProgresoAlumno(userName);

   
    // Añadir el progreso al modelo
    model.addAttribute("progreso", progreso);

    
        // Dependiendo del rol, cargar la información correspondiente y devolver la vista adecuada
        switch (role) {
            case "ROLE_COACH":
                return cargarVistaCoach(userName, model);
            case "ROLE_ALUMNO":
                return cargarVistaAlumno(userName, model);
            case "ROLE_ADMIN":
                return cargarVistaAdmin(userName, model);
            default:
                return "error"; // Manejar roles inesperados
        }
    }

    private String cargarVistaCoach(String userName, Model model) {
        Optional<CoachDTO> coachOpt = coachActividadServicio.obtenerCoachPorUserName(userName);
        if (coachOpt.isPresent()) {
            model.addAttribute("coach", coachOpt.get());
            return "coach"; // Devuelve la vista "coach.html"
        }
        return "error"; // Si no se encuentra el coach
    }

    private String cargarVistaAlumno(String userName, Model model) {
        Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.obtenerInformacionAlumnoPorUserName(userName);
        if (alumnoOpt.isPresent()) {
            model.addAttribute("alumno", alumnoOpt.get());
    
            // Obtener citas y talleres pendientes
            List<CitaDTO> citasPendientes = usuarioAlumnoServicio.obtenerCitasPendientes(userName);
            List<TallerDTO> talleresPendientes = usuarioAlumnoServicio.obtenerTalleresPendientes(userName);
    
            // Obtener citas y talleres confirmados
            List<CitaDTO> citasConfirmadas = usuarioAlumnoServicio.obtenerCitasConfirmadas(userName);
            List<TallerDTO> talleresConfirmados = usuarioAlumnoServicio.obtenerTalleresConfirmados(userName);
    
            // Añadir las listas al modelo
            model.addAttribute("citasPendientes", citasPendientes);
            model.addAttribute("talleresPendientes", talleresPendientes);
            model.addAttribute("citasConfirmadas", citasConfirmadas);
            model.addAttribute("talleresConfirmados", talleresConfirmados);
    
            // Calcular si no hay pendientes
            boolean noPendientes = talleresPendientes.isEmpty() && citasPendientes.isEmpty();
            model.addAttribute("noPendientes", noPendientes);
    
            return "alumno"; // Devuelve la vista "alumno.html"
        }
        return "error"; // Si no se encuentra el alumno
    }
    


    private String cargarVistaAdmin(String userName, Model model) {
        Optional<AdminDTO> adminOpt = usuarioAdminServicio.obtenerInformacionAdminPorUserName(userName);
        if (adminOpt.isPresent()) {
            model.addAttribute("admin", adminOpt.get());
            return "admin"; // Devuelve la vista "admin.html"
        }
        return "error"; // Si no se encuentra el admin
    }

    @GetMapping("/listadealumno/{idActividadFisica}")
    public String mostrarListaAlumnos(@PathVariable Long idActividadFisica, Model model) {
        ActividadFisicaDTO actividad = coachActividadServicio.obtenerActividadPorId(idActividadFisica);
        model.addAttribute("actividad", actividad);
        return "listaAlumnos";
    }

    @PostMapping("/paseLista/{idActividadFisica}")
    public String iniciarPaseLista(@PathVariable Long idActividadFisica, Model model) {
        // Obtener la actividad física por su ID
        ActividadFisicaDTO actividadDTO = coachActividadServicio.obtenerActividadPorId(idActividadFisica);
        
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
    
        // Verificar o crear una nueva lista para la actividad y fecha
        Entidad_Lista lista = listaServicio.obtenerOCrearLista(actividadDTO, fechaActual);
        
        // Obtener la lista de alumnos con su estado de asistencia actualizado
        List<AlumnoDTO> alumnosActualizados = asistenciaActividadFisicaServicio.obtenerAlumnosConAsistencia(actividadDTO.getAlumnos(), lista);
    
        // *** Aquí agregas el System.out.println para verificar la fecha de registro ***
        alumnosActualizados.forEach(alumno -> System.out.println("Fecha Registro: " + alumno.getFechaRegistro()));
    
        // Añadir la lista y la actividad al modelo
        model.addAttribute("lista", lista);
        model.addAttribute("actividad", actividadDTO);
        model.addAttribute("alumnos", alumnosActualizados);  // Añadir alumnos actualizados
    
        return "paseLista"; // Redirigir a la página de pase de lista
    }
    

    

@PostMapping("/guardarAsistencia/{idLista}")
public String guardarAsistencia(@PathVariable Long idLista, @RequestParam("asistencias") List<Long> asistencias) {
    // Obtener la lista de asistencia
    Entidad_Lista lista = listaServicio.obtenerListaPorId(idLista);

    // Obtener la hora actual
    LocalDateTime horaActual = LocalDateTime.now();

    // Registrar la asistencia con la hora actual
    listaServicio.guardarAsistencia(lista, asistencias, horaActual);

    // Redirigir de nuevo a la vista del coach
    return "redirect:/portal/inicio";
}

    

}
