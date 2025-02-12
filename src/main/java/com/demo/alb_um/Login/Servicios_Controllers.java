package com.demo.alb_um.Login;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.ManagerDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import com.demo.alb_um.DTOs.TallerInscripcionDTO;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Facultad.Entidad_facultad;
import com.demo.alb_um.Modulos.Facultad.FacultadServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Manager.ManagerServicio;
import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Antropometria.AntropometriaServicio;
import com.demo.alb_um.Modulos.Antropometria.Ent_Antro;
import com.demo.alb_um.Modulos.Carrera.CarreraServicio;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaServicio;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminServicio;

@Service
public class Servicios_Controllers {

    
   
    
    private final InscripcionTallerServicio inscripcionTallerServicio;

    private final AntropometriaServicio antropometriaServicio;


private final ActividadFisicaServicio actividadFisicaServicio;

@Autowired
private FacultadServicio facultadServicio;

@Autowired
private CarreraServicio carreraServicio;

    @Autowired
public Servicios_Controllers(InscripcionTallerServicio inscripcionTallerServicio, AntropometriaServicio antropometriaServicio, ActividadFisicaServicio actividadFisicaServicio) {
    this.inscripcionTallerServicio = inscripcionTallerServicio;
    this.antropometriaServicio = antropometriaServicio;
    this.actividadFisicaServicio = actividadFisicaServicio;
}

    @Autowired CoachActividadServicio coachActividadServicio;

public String cargarVistaCoach(String userName, CoachActividadServicio coachActividadServicio, Model model) {
    Optional<CoachDTO> coachOpt = coachActividadServicio.obtenerCoachPorUserName(userName);
    if (coachOpt.isPresent()) {
        CoachDTO coach = coachOpt.get();
        model.addAttribute("coach", coach);

        // Obtener actividades asociadas al coach
        List<ActividadFisicaDTO> actividades = coachActividadServicio.obtenerActividadesPorCoach(coach.getIdUsuario());
        model.addAttribute("actividades", actividades);

        return "/Vistas_Coach/Vista_General"; 
    }
    return "error"; 
}



public String cargarVistaAlumno(
    String userName,
    UsuarioAlumnoServicio usuarioAlumnoServicio,
    Model model
) {
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.obtenerInformacionAlumnoPorUserName(userName);
    if (alumnoOpt.isPresent()) {
        AlumnoDTO alumno = alumnoOpt.get();

        // Obtener citas y talleres
        List<CitaDTO> citasPendientes = usuarioAlumnoServicio.obtenerCitasPendientes(userName);
        List<TallerInscripcionDTO> talleresPendientes = usuarioAlumnoServicio.obtenerTalleresPendientes(userName);
        List<CitaDTO> citasConfirmadas = usuarioAlumnoServicio.obtenerCitasConfirmadas(userName);
        List<TallerInscripcionDTO> talleresConfirmados = usuarioAlumnoServicio.obtenerTalleresConfirmados(userName);

        // Obtener datos antropométricos de una cita confirmada de antropometría
        Optional<Ent_Antro> datosAntroOpt = antropometriaServicio.obtenerDatosAntropometricosPorCita(alumno.getIdUsuarioAlumno());
        datosAntroOpt.ifPresentOrElse(
            datosAntro -> model.addAttribute("datosAntro", datosAntro),
            () -> model.addAttribute("datosAntro", null) // Si no hay, establecer null
        );

        // Añadir datos al modelo
        model.addAttribute("alumno", alumno);
        model.addAttribute("citasPendientes", citasPendientes);
        model.addAttribute("talleresPendientes", talleresPendientes);
        model.addAttribute("citasConfirmadas", citasConfirmadas);
        model.addAttribute("talleresConfirmados", talleresConfirmados);

        // Calcular si no hay pendientes
        boolean noPendientes = talleresPendientes.isEmpty() && citasPendientes.isEmpty();
        model.addAttribute("noPendientes", noPendientes);

        return "/Vistas_Alumno/Vista_General";
    }

    model.addAttribute("mensajeError", "No se encontró información para el alumno.");
    return "error";
}

    


    
public String cargarVistaAdmin(String userName, UsuarioAdminServicio usuarioAdminServicio, Model model) {
    // Obtener información del administrador
    Optional<AdminDTO> adminOpt = usuarioAdminServicio.obtenerInformacionAdminPorUserName(userName);

    // Cargar facultades y carreras para búsquedas avanzadas
    List<Entidad_facultad> facultades = facultadServicio.obtenerTodas();
    List<Entidad_carrera> carreras = carreraServicio.obtenerTodas();
    model.addAttribute("facultades", facultades);
    model.addAttribute("carreras", carreras);

    // Validar si el administrador existe
    if (adminOpt.isPresent()) {
        AdminDTO admin = adminOpt.get();
        model.addAttribute("admin", admin);

        // Acciones según el servicio del administrador
        switch (admin.getServicio().getNombre()) {
            case "Antropometria":
                // Cargar citas pendientes para Antropometría
                List<CitaDTO> citasPendientes = usuarioAdminServicio.obtenerCitasPendientesPorServicio(
                    admin.getServicio().getIdServicio());
                model.addAttribute("citasPendientes", citasPendientes);

                // Redirigir a la vista específica de Antropometría
                return "antropometria";
            
                case "Talleres":
                try {
                    // Obtener talleres del día
                    List<TallerDTO> talleresDelDia = inscripcionTallerServicio.obtenerTalleresDelDia();
            
                    if (talleresDelDia.isEmpty()) {
                        model.addAttribute("mensajeInfo", "No hay talleres programados para hoy.");
                    } else {
                        // Filtrar talleres en curso
                        List<TallerDTO> talleresEnCurso = talleresDelDia.stream()
                            .filter(t -> t.getEstado() == EstadoTaller.EN_CURSO)
                            .collect(Collectors.toList());
            
                        // Añadir talleres al modelo
                        model.addAttribute("talleresDelDia", talleresDelDia);
                        model.addAttribute("talleresEnCurso", talleresEnCurso);
                    }
            
                    // Añadir la hora actual para referencia
                    model.addAttribute("horaActual", LocalDateTime.now());
                } catch (Exception e) {
                    System.err.println("Error al cargar talleres: " + e.getMessage());
                    e.printStackTrace();
                    model.addAttribute("error", "Error al cargar los talleres. Por favor, intente más tarde.");
                }
            
                // Redirigir a la vista específica de Talleres
                return "/Vistas_Admins_Talleres/Vista_General";

            case "Aptitud Fisica":
                // Cargar actividades físicas
                List<ActividadFisicaDTO> actividades = actividadFisicaServicio.obtenerTodasComoDTO();
                System.out.println("Actividades obtenidas: " + (actividades != null ? actividades.size() : "null"));
                model.addAttribute("actividades", actividades != null ? actividades : List.of());

                // Redirigir a la vista específica de Aptitud Física
                return "/Vistas_Admins_Aptitud/Vista_General";
            
            default:
                // Servicio no especificado, redirigir a una vista genérica
                return "/Vistas_Admins_Generales/Vista_general";
        }
    }

    // Si no se encuentra el administrador, redirigir a una vista de error
    return "error";
}


    public String cargarVistaManager(String userName, ManagerServicio managerServicio, Model model) {
        // Obtener información del manager basado en el username
        ManagerDTO manager = managerServicio.obtenerInformacionManagerPorUserName(userName);
        
        if (manager != null) {
            model.addAttribute("manager", manager); // Añadir información del manager al modelo
            model.addAttribute("mensaje", "Bienvenido al portal de Manager"); // Mensaje de bienvenida
            return "/Vistas_Manager/Vista_General"; // Renderizar la vista managerInicio
        }

        model.addAttribute("mensajeError", "No se encontró información para el manager.");
        return "error"; // Vista de error si el manager no existe
    }
    
    
}
