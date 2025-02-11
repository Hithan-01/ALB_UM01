package com.demo.alb_um.Login;

import com.demo.alb_um.Config.EmailService;
import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.RegistrarActividadDTO;
import com.demo.alb_um.DTOs.RegistroAdminDTO;
import com.demo.alb_um.DTOs.RegistroAlumnoDTO;
import com.demo.alb_um.DTOs.RegistroCoachDTO;
import com.demo.alb_um.DTOs.RegistroServicioDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Carrera.Repositorio_Carrera;
import com.demo.alb_um.Modulos.Coach.CoachActividadRepositorio;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaServicio;
import com.demo.alb_um.Modulos.Manager.ManagerServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal/manager")
public class ManagerControlador {


    @Autowired
    private ActividadFisicaServicio actividadFisicaServicio;

    @Autowired
    private ManagerServicio managerServicio;

    @Autowired CoachActividadServicio coachActividadServicio;

    @Autowired
    private CoachActividadRepositorio coachActividadRepositorio;

    @Autowired
    private Repositorio_Carrera carreraRepository;
    



    
    /**
     * Lista todas las actividades físicas agrupadas por tipo.
     */
    @GetMapping("/listar-actividades")
    public String listarActividadesFisicas(Model model) {
        // Obtener todas las actividades como DTO
        List<ActividadFisicaDTO> actividades = actividadFisicaServicio.obtenerTodasComoDTO();
    
        // Agrupar actividades por tipo (nombre)
        Map<String, List<ActividadFisicaDTO>> actividadesPorTipo = actividades.stream()
                .collect(Collectors.groupingBy(ActividadFisicaDTO::getNombre));
    
        // Log para verificar los datos
        System.out.println("Actividades agrupadas por tipo: " + actividadesPorTipo);
    
        // Pasar las actividades agrupadas al modelo
        model.addAttribute("actividadesPorTipo", actividadesPorTipo);
        return "/Vistas_Manager/Lista_Actividades";
    }
    
    @GetMapping("/eliminar-actividad/{id}")
    public String eliminarActividad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            actividadFisicaServicio.eliminarActividadFisica(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Actividad eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al eliminar la actividad.");
        }
        return "redirect:/portal/manager/listar-actividades";
    }


    @GetMapping("/alumnos-actividad/{id}")
    public String listarAlumnosPorActividad(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<AlumnoDTO> alumnos = actividadFisicaServicio.obtenerAlumnosPorActividad(id);
            model.addAttribute("alumnos", alumnos);
            return "/Vistas_Manager/Lista_Alumnos_Actividades";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo cargar la lista de alumnos: " + e.getMessage());
            return "redirect:/portal/manager/listar-actividades";
        }
    }

    @GetMapping("/mover-alumno/{idUsuarioAlumno}")
    public String mostrarOpcionesDeActividades(@PathVariable Long idUsuarioAlumno, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el alumno por su ID
            AlumnoDTO alumno = actividadFisicaServicio.obtenerAlumnoPorId(idUsuarioAlumno);
    
            // Obtener todas las actividades disponibles
            List<ActividadFisicaDTO> actividades = actividadFisicaServicio.obtenerTodasComoDTO();
    
            // Agregar datos al modelo
            model.addAttribute("alumno", alumno);
            model.addAttribute("actividades", actividades);
    
            return "/Vistas_Manager/Mover_Alumno"; // Vista donde se muestra el formulario de actividades
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cargar las opciones de actividades: " + e.getMessage());
            return "redirect:/portal/manager/listar-actividades";
        }
    }
    
    
    @PostMapping("/mover-alumno/{idUsuarioAlumno}")
    public String moverAlumno(
        @PathVariable Long idUsuarioAlumno, 
        @RequestParam Long idNuevaActividad, 
        RedirectAttributes redirectAttributes
    ) {
        try {
            // Utiliza directamente el servicio que ya tiene la lógica de movimiento
            actividadFisicaServicio.moverAlumnoAActividad(idUsuarioAlumno, idNuevaActividad);
    
            redirectAttributes.addFlashAttribute("success", "El alumno fue movido correctamente a la nueva actividad.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al mover al alumno: " + e.getMessage());
        }
        return "redirect:/portal/manager/listar-actividades";
    }
    
    @GetMapping("/crear-actividad")
    public String mostrarFormularioCrearActividad(Model model) {
        model.addAttribute("actividad", new RegistrarActividadDTO("", "", null, ""));
        return "/Vistas_Manager/Crear_Actividad";
    }

    @PostMapping("/crear-actividad")
    public String registrarActividad(String nombre, String diaSemana, String hora, String grupo, RedirectAttributes redirectAttributes) {
        try {
            // Validar y convertir la hora a Time
            if (!hora.matches("\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("Formato de hora inválido. Use el formato HH:mm.");
            }
            
    
            // Crear el DTO con la hora como String
            RegistrarActividadDTO dto = new RegistrarActividadDTO(nombre, diaSemana, hora, grupo);
    
            // Registrar la actividad
            Entidad_ActividadFisica actividad = actividadFisicaServicio.registrarActividad(dto);
    
            // Agregar mensaje de éxito
            redirectAttributes.addFlashAttribute("mensajeExito", "Actividad '" + actividad.getNombre() + "' creada exitosamente.");
            return "redirect:/portal/manager/listar-actividades";
        } catch (IllegalArgumentException e) {
            // Manejar errores de validación
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Manejar otros errores
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al crear la actividad.");
        }
    
        // Redirigir a la página de creación en caso de error
        return "redirect:/portal/manager/crear-actividad";
    }
    

        // Mostrar formulario para asignar Coach
    @GetMapping("/asignar-coach/{idActividad}")
    public String mostrarFormularioAsignarCoach(@PathVariable Long idActividad, Model model) {
        List<CoachDTO> coaches = coachActividadServicio.obtenerCoaches(); // Obtener todos los coaches
        model.addAttribute("coaches", coaches);
        model.addAttribute("idActividad", idActividad);
        return "/Vistas_Manager/Asignar_Coach"; // Retorna la vista con el formulario
    }

    // Asignar Coach a la actividad
    @PostMapping("/asignar-coach")
    public String asignarCoach(@RequestParam Long idActividad, @RequestParam Long coachId, RedirectAttributes redirectAttributes) {
        try {
            actividadFisicaServicio.asignarCoach(idActividad, coachId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Coach asignado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al asignar el Coach: " + e.getMessage());
        }
        return "redirect:/portal/manager/listar-actividades";
    }

    // Mostrar formulario para cambiar Coach
    @GetMapping("/cambiar-coach/{idActividad}")
    public String mostrarFormularioCambiarCoach(@PathVariable Long idActividad, Model model) {
        Ent_CoachActividad coachActual = coachActividadRepositorio.findByActividadFisicaId(idActividad)
                .orElseThrow(() -> new IllegalArgumentException("No hay un Coach asignado a esta actividad"));
    
        List<CoachDTO> coachesDisponibles = coachActividadServicio.obtenerCoachesDisponibles(coachActual.getUsuario().getIdUsuario());
    
        model.addAttribute("coaches", coachesDisponibles);
        model.addAttribute("idActividad", idActividad);
        return "/Vistas_Manager/Cambiar_Coach";
    }
    

// Procesar el cambio de Coach
@PostMapping("/cambiar-coach")
public String cambiarCoach(@RequestParam Long idActividad, @RequestParam Long coachId, RedirectAttributes redirectAttributes) {
    try {
        actividadFisicaServicio.cambiarCoach(idActividad, coachId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Coach cambiado correctamente.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al cambiar el Coach: " + e.getMessage());
    }
    return "redirect:/portal/manager/editar/" + idActividad; 
}


// Método para mostrar el formulario de edición
@GetMapping("/editar/{id}")
public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
    // Obtener la actividad por ID
    Entidad_ActividadFisica actividad = actividadFisicaServicio.obtenerPorId(id);

    // Convertir a DTO
    ActividadFisicaDTO actividadDTO = actividadFisicaServicio.convertirAActividadFisicaDTO(actividad);

    // Añadir el DTO al modelo
    model.addAttribute("actividadDTO", actividadDTO);
    return "/Vistas_Manager/Editar_Actividad";
}




    // Método para procesar el formulario de edición
    @PostMapping("/editar/{id}")
    public String editarActividad(@PathVariable Long id, @ModelAttribute RegistrarActividadDTO dto, RedirectAttributes redirectAttributes) {
        actividadFisicaServicio.editarActividad(id, dto);
        redirectAttributes.addFlashAttribute("mensaje", "Actividad actualizada con éxito");
        return "redirect:/portal/manager/listar-actividades";
    }
    
    @GetMapping("/usuarios")
    public String mostrarUsuarios() {
        return "/Vistas_Manager/Vista_De_Registros";
    }

    @GetMapping("/registrar-alumno")
    public String mostrarFormulario(Model model) {
        model.addAttribute("registroAlumno", new RegistroAlumnoDTO());
        model.addAttribute("carreras", carreraRepository.findAll()); // Cargar todas las carreras disponibles
        return "/Vistas_Manager/Formulario_Registro_Alumno"; // Vista HTML
    }
    
    // Procesar formulario y guardar alumno
    @PostMapping("/registrar-alumno")
    public String registrarAlumno(@ModelAttribute("registroAlumno") RegistroAlumnoDTO registroAlumnoDTO) {
        managerServicio.registrarAlumno(registroAlumnoDTO); // Usar el servicio para registrar
        return "redirect:/portal/manager/usuarios?success"; // Redirige con mensaje de éxito
    }
    

    @GetMapping("/registrar-admin")
    public String mostrarFormularioAdmin(Model model) {
        model.addAttribute("registroAdmin", new RegistroAdminDTO());
        model.addAttribute("servicios", managerServicio.obtenerServicios()); // Asegúrate de enviar la lista de servicios
        return "/Vistas_Manager/Formulario_Registro_Admin";
    }
    

    @PostMapping("/registrar-admin")
    public String registrarAdmin(@ModelAttribute("registroAdmin") RegistroAdminDTO dto, Model model) {
        managerServicio.registrarAdmin(dto);
    
        // Agregar mensaje de éxito
        model.addAttribute("successMessage", "Administrador registrado exitosamente.");
        
        // Volver a cargar la lista de servicios
        model.addAttribute("registroAdmin", new RegistroAdminDTO());
        model.addAttribute("servicios", managerServicio.obtenerServicios());
    
        return "/Vistas_Manager/Formulario_Registro_Admin"; // Mantiene en la misma página
    }
    
    // Mostrar el formulario para crear un servicio
    @GetMapping("/registrar-servicio")
    public String Mostrarformulario(Model model) {
        model.addAttribute("registroServicio", new RegistroServicioDTO());
        return "/Vistas_Manager/Formulario_Crear_Servicio";
    }

// Procesar el formulario
@PostMapping("/registrar-servicio")
public String registrarServicio(@ModelAttribute("registroServicio") RegistroServicioDTO dto, RedirectAttributes redirectAttributes) {
    managerServicio.registrarServicio(dto);
    redirectAttributes.addFlashAttribute("success", "Servicio registrado con éxito.");
    return "redirect:/portal/manager/registrar-servicio"; // 🔥 Regresa a la misma vista
}


    @GetMapping("/servicios")
    public String listarServicios(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("servicios", managerServicio.obtenerServicios());
        if (success != null) {
            model.addAttribute("mensajeExito", "Servicio creado exitosamente");
        }
        return "/Vistas_Manager/Lista_Servicios";
    }


    @GetMapping("/registrar-coach")
    public String mostrarFormularioRegistroCoach(Model model) {
        model.addAttribute("registroCoach", new RegistroCoachDTO());
        return "/Vistas_Manager/Formulario_Registro_Coach"; // Nombre de la vista HTML
    }

    @PostMapping("/registrar-coach")
    public String registrarCoach(RegistroCoachDTO registroCoach) {
        managerServicio.registrarCoach(registroCoach);
        return "redirect:/portal/manager/usuarios?success";
    }


    @Autowired
    private EmailService emailService;

/**
     * Enviar correo individual a un usuario.
     */
    @PostMapping("/enviar-individual")
    public String enviarCorreoIndividual(@RequestParam String destinatario, 
                                         @RequestParam String asunto, 
                                         @RequestParam String mensaje,
                                         RedirectAttributes redirectAttributes) {
        try {
            emailService.enviarCorreoIndividual(destinatario, asunto, mensaje);
            redirectAttributes.addFlashAttribute("mensajeExito", "Correo enviado correctamente a " + destinatario);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al enviar el correo: " + e.getMessage());
        }
        return "redirect:/portal/inicio";
    }

    /**
     * Enviar correos a todos los alumnos registrados.
     */
    @PostMapping("/enviar-masivo")
    public String enviarCorreoMasivo(@RequestParam String asunto, 
                                     @RequestParam String mensaje,
                                     RedirectAttributes redirectAttributes) {
        try {
            emailService.enviarCorreoMasivo(asunto, mensaje);
            redirectAttributes.addFlashAttribute("mensajeExito", "Correos masivos enviados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al enviar los correos: " + e.getMessage());
        }
        return "redirect:/portal/inicio";
    }
}
