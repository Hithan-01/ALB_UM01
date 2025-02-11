package com.demo.alb_um.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminServicio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Horario_servicio.HorarioServicioServicio;
import com.demo.alb_um.Modulos.Manager.ManagerServicio;

@Controller
@RequestMapping("/portal")
public class PortalControlador {

    private final CoachActividadServicio coachActividadServicio;
    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final UsuarioAdminServicio usuarioAdminServicio;
    private final Servicios_Controllers serviciosControllers;
    private final ManagerServicio managerServicio;

    
    @Autowired
    public PortalControlador(CoachActividadServicio coachActividadServicio, 
                             UsuarioAlumnoServicio usuarioAlumnoServicio, 
                             UsuarioAdminServicio usuarioAdminServicio, 
                             HorarioServicioServicio horarioServicio, 
                             Servicios_Controllers serviciosControllers,
                             ManagerServicio managerServicio) {
        this.coachActividadServicio = coachActividadServicio;
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.usuarioAdminServicio = usuarioAdminServicio;
        this.serviciosControllers = serviciosControllers;
        this.managerServicio = managerServicio;
       
    }

    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // Calcular el progreso del alumno (si aplica)
        if ("ROLE_ALUMNO".equals(role)) {
            int progreso = usuarioAlumnoServicio.calcularProgresoAlumno(userName);
            model.addAttribute("progreso", progreso);
        }

        // Delegar la carga de vista según el rol
        switch (role) {
            case "ROLE_COACH":
                return serviciosControllers.cargarVistaCoach(userName, coachActividadServicio, model);
            case "ROLE_ALUMNO":
                return serviciosControllers.cargarVistaAlumno(userName, usuarioAlumnoServicio, model);
            case "ROLE_ADMIN":
                return serviciosControllers.cargarVistaAdmin(userName, usuarioAdminServicio, model);
                case "ROLE_MANAGER":
                return serviciosControllers.cargarVistaManager(userName, managerServicio, model);
            default:
            System.out.println("Rol no reconocido: " + role);
            return "error"; // Manejar roles inesperados
        }
    }


}
