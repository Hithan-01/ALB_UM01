package com.demo.alb_um.Login;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaServicio;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminRepositorio;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoServicio;
import com.demo.alb_um.Modulos.Antropometria.AntropometriaRepositorio;
import com.demo.alb_um.Modulos.Antropometria.AntropometriaServicio;
import com.demo.alb_um.Modulos.Antropometria.Ent_Antro;
import com.demo.alb_um.Modulos.Asitencia_Act.Ent_AsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Asitencia_Act.RepositorioAsistenciaActividadFisica;
import com.demo.alb_um.Modulos.Carrera.CarreraServicio;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import com.demo.alb_um.Modulos.Carrera.Repositorio_Carrera;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Coach.CoachActividadRepositorio;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Coach.Ent_CoachActividad;
import com.demo.alb_um.Modulos.Facultad.Entidad_facultad;
import com.demo.alb_um.Modulos.Facultad.FacultadServicio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Manager.ManagerServicio;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.AlumnoBusquedaDTO;
import com.demo.alb_um.DTOs.AlumnoDTO;
import com.demo.alb_um.DTOs.AlumnoTallerDTO;
import com.demo.alb_um.DTOs.BusquedaFaltas;
import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.DTOs.RegistrarActividadDTO;
import com.demo.alb_um.DTOs.RegistroAlumnoDTO;
import com.demo.alb_um.DTOs.RegistroCoachDTO;


@Controller
@RequestMapping("/portal/admin")
@RequiredArgsConstructor
public class AdminControlador {

    // Servicios principales
    private final FacultadServicio facultadServicio;
    private final CarreraServicio carreraServicio;
    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final InscripcionTallerServicio inscripcionTallerServicio;
    private final AntropometriaServicio antropometriaServicio;
    private final ManagerServicio managerServicio;
    private final ActividadFisicaServicio actividadFisicaServicio;
    private final CoachActividadServicio coachActividadServicio;


    // Repositorios necesarios
    private final UsuarioAdminRepositorio usuarioAdminRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final AntropometriaRepositorio antropometriaRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;
    private final RepositorioAsistenciaActividadFisica asistenciaActividadFisicaRepositorio;
    private final Repositorio_Carrera carreraRepository;
    private final CoachActividadRepositorio coachActividadRepositorio;

// Buscar Alumno - Para administradores que no sean de Antropometría
@GetMapping("/general/buscarAlumno")
public String buscarAlumno(@RequestParam("search") String search, Model model, Principal principal) {
    // Obtener el administrador actual por su nombre de usuario
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());

    // Validar que el administrador exista y no sea de Antropometría
    if (adminOpt.isEmpty() || esAdminAntropometria(adminOpt.get())) {
        model.addAttribute("mensajeError", "No tienes permiso para realizar esta acción.");
        return "redirect:/portal/inicio"; 
    }

    // Obtener el servicio del administrador
    Ent_UsuarioAdmin admin = adminOpt.get();
    Ent_Servicio servicioAdmin = admin.getServicio();

    // Buscar al alumno por su username
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorUsername(search);

    // Validar si se encontró el alumno
    if (alumnoOpt.isEmpty()) {
        model.addAttribute("mensajeError", "Alumno no encontrado.");
        return "redirect:/portal/inicio"; 
    }

    AlumnoDTO alumno = alumnoOpt.get();

    // Verificar si el alumno ya tiene una cita confirmada para el servicio del admin
    boolean tieneCitaConfirmada = citaServicio.alumnoTieneCitaConfirmada(alumno.getIdUsuarioAlumno(), servicioAdmin.getIdServicio());
    if (tieneCitaConfirmada) {
        // Redirigir a la nueva página con el mensaje de advertencia
        model.addAttribute("mensajeAdvertencia", "El alumno ya tiene una cita confirmada para este servicio.");
        return "/Vistas_Admins_Generales/Cita_Confirmada"; // Redirigir a la nueva página
    }

    // Si no tiene cita confirmada, redirigir a la página de validación del alumno
    model.addAttribute("alumno", alumno);
    model.addAttribute("admin", admin);
    model.addAttribute("mensajeExito", "El alumno está listo para registrar una nueva cita.");
    return "/Vistas_Admins_Generales/Informacion_alumno"; // Página para gestionar la cita
}


@PostMapping("/general/generarAsistencia")
public String generarAsistencia(@RequestParam("alumnoId") Long alumnoId,
                                @RequestParam(value = "estadoAsistencia", defaultValue = "Asistió") String estadoAsistencia,
                                Model model, Principal principal) {

    // Verificar si el administrador tiene permisos
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
    if (adminOpt.isEmpty() || esAdminAntropometria(adminOpt.get())) {
        model.addAttribute("error", "No tienes permiso para realizar esta acción.");
        return "/Vistas_Admins_Generales/Informacion_alumno"; // Regresar a la misma página
    }

    // Buscar al alumno por su ID
    Optional<AlumnoDTO> alumnoOpt = usuarioAlumnoServicio.buscarAlumnoPorId(alumnoId);
    if (alumnoOpt.isEmpty()) {
        model.addAttribute("error", "Alumno no encontrado.");
        return "/Vistas_Admins_Generales/Informacion_alumno"; // Regresar a la misma página
    }

    // Generar asistencia
    try {
        AlumnoDTO alumno = alumnoOpt.get();
        citaServicio.generarCita(adminOpt.get(), alumno.getIdUsuarioAlumno(), estadoAsistencia);
        model.addAttribute("mensaje", "Asistencia generada correctamente.");
        model.addAttribute("alumno", alumno); // Asegurarse de incluir los datos del alumno
    } catch (Exception e) {
        model.addAttribute("error", "Ocurrió un error al generar la asistencia: " + e.getMessage());
    }

    // Volver a la misma página con mensajes actualizados
    return "/Vistas_Admins_Generales/Informacion_alumno";
}


@GetMapping("/antropometria/{id}/datos")
public String mostrarFormularioDatos(@PathVariable Long id, Model model) {
    // Buscar los datos antropométricos existentes o inicializarlos si no existen
    Ent_Antro datosAntro = antropometriaRepositorio.findByCitaId(id)
        .orElse(new Ent_Antro()); // Si no existe, crear una nueva instancia

    // Obtener la cita asociada
    Ent_Cita cita = datosAntro.getCita();
    if (cita == null) {
        cita = citaRepositorio.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
    }

    // Obtener el usuario asociado a la cita
    Entidad_Usuario_Alumno alumno = cita.getUsuarioAlumno();
    Entidad_Usuario usuario = alumno.getUsuario();

    // Calcular edad cronológica
    Integer edad = calcularEdadPorFechaNacimiento(usuario.getFecha_nacimiento());
    String genero = usuario.getGenero();

    // Pasar los datos al modelo
    model.addAttribute("datosAntro", datosAntro);
    model.addAttribute("citaId", id);
    model.addAttribute("edad", edad);
    model.addAttribute("sexo", genero);

    return "formularioDatosAntro";
}

private Integer calcularEdadPorFechaNacimiento(LocalDate fechaNacimiento) {
    return fechaNacimiento != null ? Period.between(fechaNacimiento, LocalDate.now()).getYears() : null;
}



@PostMapping("/antropometria/{id}/datos")
public String guardarDatosAntroYValidarCita(
    @PathVariable Long id,
    @ModelAttribute("datosAntro") Ent_Antro datosAntro,
    Principal principal,
    RedirectAttributes redirectAttributes) {

    // Obtener al administrador que realiza la acción
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
    if (adminOpt.isEmpty() || !esAdminAntropometria(adminOpt.get())) {
        redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para realizar esta acción.");
        return "redirect:/portal/admin";
    }

    try {
        // Llamar al servicio para guardar los datos y validar la cita
        antropometriaServicio.guardarDatosAntroYValidarCita(id, datosAntro, adminOpt.get());
        redirectAttributes.addFlashAttribute("mensajeExito", "Datos registrados y cita validada correctamente.");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
    }

    return "redirect:/portal/inicio"; 
}


    // Método compartido para verificar si un admin es de Antropometría
    private boolean esAdminAntropometria(Ent_UsuarioAdmin admin) {
        return "Antropometria".equals(admin.getServicio().getNombre());
    }


    @PostMapping("/taller/{id}/detener")
    public String detenerTaller(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            // Llamar al servicio para finalizar el taller
            inscripcionTallerServicio.finalizarTaller(id);
            
            // Agregar mensaje de éxito
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "El taller ha sido finalizado exitosamente. Puede proceder a registrar la hora de salida.");
        } catch (RuntimeException e) {
            // Agregar mensaje de error en caso de excepción
            redirectAttributes.addFlashAttribute("error", 
                "Error al finalizar el taller: " + e.getMessage());
        } catch (Exception e) {
            // Manejo general de excepciones inesperadas
            redirectAttributes.addFlashAttribute("error", 
                "Ocurrió un error inesperado al finalizar el taller. Por favor, inténtelo nuevamente.");
        }
        
        // Redirigir a la página principal u otra vista
        return "redirect:/portal/inicio";
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

@PostMapping("/taller/{id}/registrarSalida")
@ResponseBody
public Map<String, Object> registrarSalida(
                @PathVariable Long id,
                @RequestParam String identificador) {
            return inscripcionTallerServicio.registrarHoraSalida(id, identificador);
        }


@GetMapping("/talleres/nuevo")
public String mostrarFormularioNuevoTaller(Model model, Principal principal, RedirectAttributes redirectAttributes) {
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
    if (adminOpt.isEmpty() || !esAdminDeTalleres(adminOpt.get())) {
        redirectAttributes.addFlashAttribute("error", "No tienes permisos para crear talleres.");
        return "redirect:/Alb_Um/portal/inicio";
    }

    model.addAttribute("taller", new Ent_Taller());
    return "/Vistas_Admins_Talleres/Crear_Taller"; // Vista Thymeleaf para crear talleres
}

private boolean esAdminDeTalleres(Ent_UsuarioAdmin admin) {
    return "Talleres".equalsIgnoreCase(admin.getServicio().getNombre());
}


@PostMapping("/talleres/nuevo")
public String registrarTaller(
        @ModelAttribute Ent_Taller taller,
        Principal principal,
        RedirectAttributes redirectAttributes) {

    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
    if (adminOpt.isEmpty() || !esAdminDeTalleres(adminOpt.get())) {
        redirectAttributes.addFlashAttribute("error", "No tienes permisos para crear talleres.");
        return "redirect:/portal/admin/talleres/nuevo";
    }

    try {
        // Registrar el taller usando el servicio
        inscripcionTallerServicio.registrarTaller(taller);
        redirectAttributes.addFlashAttribute("mensajeExito", "Taller registrado exitosamente.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al registrar el taller: " + e.getMessage());
        return "redirect:/portal/admin/talleres/nuevo"; // Asegúrate de redirigir de vuelta al formulario
    }

    return "redirect:/portal/admin/talleres/nuevo";
}

@GetMapping("/talleres")
public String listarTalleres(Model model, Principal principal, RedirectAttributes redirectAttributes) {
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(principal.getName());
    if (adminOpt.isEmpty() || !esAdminDeTalleres(adminOpt.get())) {
        redirectAttributes.addFlashAttribute("error", "No tienes permisos para gestionar talleres.");
        return "redirect:/portal/inicio";
    }

    List<Ent_Taller> talleres = inscripcionTallerServicio.obtenerTodosTalleres();
    model.addAttribute("talleres", talleres);
    return "/Vistas_Admins_Talleres/Lista_De_Talleres"; // Vista Thymeleaf para mostrar talleres
}

@GetMapping("/talleres/{id}/detalles")
public String verDetallesTaller(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Ent_Taller> tallerOpt = inscripcionTallerServicio.obtenerTallerPorId(id);
    if (tallerOpt.isPresent()) {
        model.addAttribute("taller", tallerOpt.get());
        return "/Vistas_Admins_Talleres/Detalles_Taller";
    } else {
        model.addAttribute("error", "El taller no fue encontrado.");
        return "error";
    }
}


@GetMapping("/talleres/{id}/editar")
public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Ent_Taller> tallerOpt = inscripcionTallerServicio.obtenerTallerPorId(id);
    if (tallerOpt.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "Taller no encontrado.");
        return "redirect:/portal/admin/talleres";
    }

    model.addAttribute("taller", tallerOpt.get()); // Objeto con idTaller incluido
    return "/Vistas_Admins_Talleres/Editar_Taller"; // Vista para editar talleres
}


@PostMapping("/talleres/{id}/editar")
public String editarTaller(
        @PathVariable Long id,
        @ModelAttribute Ent_Taller tallerActualizado,
        RedirectAttributes redirectAttributes) {
    try {
        inscripcionTallerServicio.actualizarTaller(id, tallerActualizado);
        redirectAttributes.addFlashAttribute("mensajeExito", "Taller actualizado correctamente.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al actualizar el taller: " + e.getMessage());
    }
    return "redirect:/portal/admin/talleres";
}

@PostMapping("/talleres/{id}/eliminar")
public String eliminarTaller(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        inscripcionTallerServicio.eliminarTaller(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Taller eliminado correctamente.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al eliminar el taller: " + e.getMessage());
    }
    return "redirect:/portal/admin/talleres";
}

@GetMapping("/talleres/{id}/alumnos")
public String verAlumnos(@PathVariable Long id, Model model) {
    // Obtener el taller por ID
    Ent_Taller taller = inscripcionTallerServicio.obtenerTallerPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("El taller no existe."));

    // Obtener las listas de alumnos por estado
    Map<String, List<AlumnoTallerDTO>> alumnosPorEstado = inscripcionTallerServicio.getAlumnosPorEstado(id, taller.getEstado());

    // Agregar datos al modelo
    model.addAttribute("taller", taller);

    // Manejar casos según el estado del taller
    switch (taller.getEstado()) {
        case PROGRAMADO:
            model.addAttribute("alumnosInscritos", alumnosPorEstado.get("inscritos"));
            break;

        case REGISTRO_ABIERTO:
            model.addAttribute("alumnosLlegaron", alumnosPorEstado.get("llegaron"));
            break;

        case EN_CURSO:
            model.addAttribute("alumnosLlegaron", alumnosPorEstado.get("llegaron"));
            model.addAttribute("alumnosNoLlegaron", alumnosPorEstado.get("noLlegaron"));
            break;

        case CERRADO:
        case FINALIZADO:
            model.addAttribute("alumnosInscritos", alumnosPorEstado.get("inscritos"));
            model.addAttribute("alumnosAsistieron", alumnosPorEstado.get("asistieron"));
            break;

        default:
            throw new IllegalStateException("Estado del taller no reconocido: " + taller.getEstado());
    }

    return "/Vistas_Admins_Talleres/Lista_Alumnos"; // Vista correspondiente
}



     // Manejador de errores para este controlador
    @ExceptionHandler(Exception.class)
    public String manejarError(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/portal/admin/taller/listado";
    }

    @GetMapping("/buscar-alumno")
    public String buscarAlumnoPorMatricula(@RequestParam("username") String username, Model model) {
        Optional<Entidad_Usuario> usuarioOpt = usuarioRepositorio.findByUserName(username);
        if (usuarioOpt.isPresent()) {
            Entidad_Usuario usuario = usuarioOpt.get();
            Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findByUsuario(usuario);
    
            if (alumnoOpt.isPresent()) {
                Long alumnoId = alumnoOpt.get().getIdUsuarioAlumno();
                return "redirect:/portal/admin/detalle-alumno/" + alumnoId; // Redirige al método mostrarDetalleAlumno
            }
        }
        model.addAttribute("error", "No se encontró ningún alumno con esa matrícula.");
        return "/Vistas_Admins_Aptitud/Vista_General"; // Regresa a la página de administrador con el mensaje de error
    }

    @GetMapping("/detalle-alumno/{id}")
    public String mostrarDetalleAlumno(@PathVariable Long id, Model model) {
        // Obtener información del alumno
        Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findById(id);
    
        if (alumnoOpt.isPresent()) {
            Entidad_Usuario_Alumno alumno = alumnoOpt.get();
            model.addAttribute("alumno", alumno);
    
            // Obtener la facultad asociada
            String facultad = null;
            if (alumno.getCarrera() != null && alumno.getCarrera().getFacultad() != null) {
                facultad = alumno.getCarrera().getFacultad().getNombre();
            }
            model.addAttribute("facultad", facultad);
    
            // Consulta directa para asistencias, faltas y justificadas
            List<Map<String, Object>> actividades = alumno.getAlumnoActividades().stream().map(relacion -> {
                Entidad_ActividadFisica actividad = relacion.getActividadFisica();
    
                // Contar asistencias, faltas y justificadas directamente
                long asistencias = asistenciaActividadFisicaRepositorio.countByUsuarioAlumnoAndEstadoFalta(alumno, Ent_AsistenciaActividadFisica.EstadoFalta.PRESENTE);
                long faltas = asistenciaActividadFisicaRepositorio.countByUsuarioAlumnoAndEstadoFalta(alumno, Ent_AsistenciaActividadFisica.EstadoFalta.FALTA);
                long justificadas = asistenciaActividadFisicaRepositorio.countByUsuarioAlumnoAndEstadoFalta(alumno, Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA);
    
                // Crear mapa con la información de la actividad
                Map<String, Object> actividadInfo = new HashMap<>();
                actividadInfo.put("idActividadFisica", actividad.getIdActividadFisica());
                actividadInfo.put("nombre", actividad.getNombre());
                actividadInfo.put("diaSemana", actividad.getDiaSemana());
                actividadInfo.put("hora", actividad.getHora());
                actividadInfo.put("grupo", actividad.getGrupo());
                actividadInfo.put("asistencias", asistencias);
                actividadInfo.put("faltas", faltas);
                actividadInfo.put("justificadas", justificadas);
    
                return actividadInfo;
            }).collect(Collectors.toList());
    
            model.addAttribute("actividades", actividades);
            return "/Vistas_Admins_Aptitud/Resultados_Busqueda_Indv";
        } else {
            model.addAttribute("error", "No se encontró al alumno.");
            return "redirect:/portal/inicio";
        }
    }
    
    
    
    
    @GetMapping("/ver-detalles-asistencias/{idAlumno}")
    public String verDetallesAsistencias(@PathVariable Long idAlumno, Model model) {
        // Buscar al alumno por su ID
        Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findById(idAlumno);
    
        if (alumnoOpt.isPresent()) {
            Entidad_Usuario_Alumno alumno = alumnoOpt.get();
    
            // Obtener todas las asistencias del alumno, independientemente de la actividad actual
            List<Map<String, Object>> asistencias = alumno.getAsistencias().stream()
                .map(asistencia -> {
                    Map<String, Object> asistenciaInfo = new HashMap<>();
                    asistenciaInfo.put("fechaRegistro", asistencia.getFechaRegistro());
                    asistenciaInfo.put("estadoFalta", asistencia.getEstadoFalta() != null ? asistencia.getEstadoFalta().name() : "Sin estado");
                    asistenciaInfo.put("idAsistenciaActividadFisica", asistencia.getIdAsistenciaActividadFisica());
                    asistenciaInfo.put("justificadoPor", asistencia.getJustificadoPor() != null ? asistencia.getJustificadoPor() : "N/A");
                    asistenciaInfo.put("comentario", 
                        (asistencia.getComentarioJustificacion() != null && !asistencia.getComentarioJustificacion().isEmpty()) 
                        ? asistencia.getComentarioJustificacion() 
                        : "Sin comentario");
                    asistenciaInfo.put("fechaJustificacion", asistencia.getFechaJustificacion() != null ? asistencia.getFechaJustificacion() : null);
    
                    // Información adicional sobre la actividad asociada
                    asistenciaInfo.put("actividad", asistencia.getLista().getActividadFisica().getNombre());
                    asistenciaInfo.put("diaSemana", asistencia.getLista().getActividadFisica().getDiaSemana());
                    asistenciaInfo.put("hora", asistencia.getLista().getActividadFisica().getHora());
    
                    return asistenciaInfo;
                })
                .collect(Collectors.toList());
    
            System.out.println("Asistencias cargadas: " + asistencias);
    
            model.addAttribute("alumno", alumno);
            model.addAttribute("asistencias", asistencias);
    
            return "/Vistas_Admins_Aptitud/Detalle_Asistencias_Alumno";
        } else {
            model.addAttribute("error", "No se encontró al alumno.");
            return "/Vistas_Admins_Aptitud/Busqueda_Individual";
        }
    }
    
    

    @PostMapping("/justificar-asistencia/{id}")
    public String justificarAsistencia(
            @PathVariable Long id,
            @RequestParam String comentario, // Captura del comentario
            Principal principal, // Usuario autenticado
            RedirectAttributes redirectAttributes) {
    
        Optional<Ent_AsistenciaActividadFisica> asistenciaOpt = asistenciaActividadFisicaRepositorio.findById(id);
    
        if (asistenciaOpt.isPresent()) {
            Ent_AsistenciaActividadFisica asistencia = asistenciaOpt.get();
    
            // Verificar que se pueda justificar
            if (asistencia.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.FALTA) {
                // Capturar el nombre del administrador
                String nombreAdmin = principal.getName(); // Usuario autenticado
    
                // Actualizar datos de justificación
                asistencia.setEstadoFalta(Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA);
                asistencia.setJustificadoPor(nombreAdmin);
                asistencia.setFechaJustificacion(LocalDateTime.now()); // Fecha de justificación
                asistencia.setComentarioJustificacion(comentario);
    
                asistenciaActividadFisicaRepositorio.save(asistencia);
    
                redirectAttributes.addFlashAttribute("mensaje", "Falta justificada correctamente por " + nombreAdmin);
            } else {
                redirectAttributes.addFlashAttribute("error", "Solo se pueden justificar asistencias en estado 'FALTA'.");
            }
    
            // Redirigir a los detalles de asistencias
          
            Long idAlumno = asistencia.getUsuarioAlumno().getIdUsuarioAlumno();
            return "redirect:/portal/admin/ver-detalles-asistencias/"  + idAlumno;
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la asistencia para justificar.");
            return "redirect:/portal/admin/detalleAlumno";
        }
    }

    @PostMapping("/justificar-asistencias")
    public String justificarAsistencias(
            @RequestParam List<Long> ids, // Lista de IDs de asistencias a justificar
            @RequestParam(required = false) String comentario, // Comentario opcional
            Principal principal, // Usuario autenticado
            RedirectAttributes redirectAttributes) {
    
        // Validar que se recibieron IDs
        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se proporcionaron asistencias para justificar.");
            return "redirect:/portal/admin/resultados-busqueda-faltas";
        }
    
        // Obtener las asistencias en masa
        List<Ent_AsistenciaActividadFisica> asistencias = asistenciaActividadFisicaRepositorio.findAllById(ids);
    
        // Validar si se encontraron asistencias
        if (asistencias.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se encontraron asistencias para los IDs proporcionados.");
            return "redirect:/portal/admin/resultados-busqueda-faltas";
        }
    
        // Capturar el nombre del administrador
        String nombreAdmin = principal.getName();
    
        // Filtrar solo las asistencias con estado FALTA
        List<Ent_AsistenciaActividadFisica> faltasAJustificar = asistencias.stream()
                .filter(asistencia -> asistencia.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.FALTA)
                .toList();
    
        // Justificar asistencias válidas
        if (!faltasAJustificar.isEmpty()) {
            for (Ent_AsistenciaActividadFisica falta : faltasAJustificar) {
                falta.setEstadoFalta(Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA);
                falta.setJustificadoPor(nombreAdmin);
                falta.setFechaJustificacion(LocalDateTime.now());
                falta.setComentarioJustificacion(comentario != null && !comentario.isBlank() ? comentario : "Justificación masiva");
            }
    
            // Guardar todas las asistencias actualizadas
            asistenciaActividadFisicaRepositorio.saveAll(faltasAJustificar);
    
            int totalJustificadas = faltasAJustificar.size();
            int totalPendientes = asistencias.size() - totalJustificadas;
    
            redirectAttributes.addFlashAttribute("mensaje",
                "Se justificaron " + totalJustificadas + " faltas correctamente."
                + (totalPendientes > 0 ? " " + totalPendientes + " asistencias no requerían justificación." : ""));
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontraron faltas para justificar.");
        }
    
        return "redirect:/portal/inicio"; // Redirige a los resultados de búsqueda
    }
    
    
    
    @GetMapping("/busqueda-avanzada")
    public String busquedaAvanzada(
        @RequestParam(required = false) Long idFacultad,
        @RequestParam(required = false) Long idCarrera,
        @RequestParam(required = false) Integer numeroFaltas,
        @RequestParam(required = false) String semestre,
        @RequestParam(required = false) String filtroFaltas, // Nuevo parámetro
        Model model) {
    
        // Llamada al servicio sin el parámetro de fecha
        List<AlumnoBusquedaDTO> alumnos = usuarioAlumnoServicio.buscarAvanzado(idFacultad, idCarrera, numeroFaltas, semestre, filtroFaltas);
    
        // Agregar resultados al modelo para la vista
        model.addAttribute("resultados", alumnos);
    
        // Pasar nuevamente las facultades y carreras para mantener el formulario
        List<Entidad_facultad> facultades = facultadServicio.obtenerTodas();
        List<Entidad_carrera> carreras = carreraServicio.obtenerTodas();
        model.addAttribute("facultades", facultades);
        model.addAttribute("carreras", carreras);
    
        // Agregar el valor de filtroFaltas al modelo para mantener el estado en el formulario
        model.addAttribute("filtroFaltas", filtroFaltas);
    
        return "/Vistas_Admins_Aptitud/Resultados_Busqueda_Avzda"; // Nombre de la plantilla HTML donde se mostrarán los resultados
    }

    @GetMapping("/busqueda-faltas")
public String buscarFaltas(
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
        @RequestParam(required = false) Long idFacultad,
        @RequestParam(required = false) Long idCarrera,
        @RequestParam(required = false) String semestre,
        Model model) {

    // Llamada al servicio
    List<BusquedaFaltas> faltas = usuarioAlumnoServicio.buscarFaltas(fecha, idFacultad, idCarrera, semestre);

    // Agregar resultados al modelo
    model.addAttribute("faltas", faltas);

    // Pasar facultades y carreras para los filtros
    List<Entidad_facultad> facultades = facultadServicio.obtenerTodas();
    List<Entidad_carrera> carreras = carreraServicio.obtenerTodas();
    model.addAttribute("facultades", facultades);
    model.addAttribute("carreras", carreras);

    return "/Vistas_Admins_Aptitud/Resultados_Busqueda_Faltas";
}

    
    @GetMapping("/registrar-alumno")
    public String mostrarFormulario(Model model) {
        model.addAttribute("registroAlumno", new RegistroAlumnoDTO());
    
        // Obtener y procesar actividades físicas
        List<Map<String, String>> actividades = actividadFisicaServicio.obtenerTodas()
                .stream()
                .map(actividad -> {
                    Map<String, String> actividadMap = new HashMap<>();
                    actividadMap.put("id", actividad.getIdActividadFisica().toString());
                    actividadMap.put("descripcion", String.format("%s %s %s",
                            actividad.getNombre(),
                            actividad.getDiaSemana(),
                            actividad.getHora().toString()));
                    return actividadMap;
                })
                .toList();
    
        model.addAttribute("actividades", actividades);
        model.addAttribute("carreras", carreraRepository.findAll()); // Cargar todas las carreras disponibles
        return "/Vistas_Admins_Aptitud/Formulario_Registro_Alumno"; // Vista HTML
    }
    
    

    @PostMapping("/registrar-alumno")
    public String registrarAlumno(@ModelAttribute("registroAlumno") RegistroAlumnoDTO registroAlumnoDTO, Model model) {
        managerServicio.registrarAlumno(registroAlumnoDTO); // Usar el servicio para registrar
    
        // Agregar mensaje de éxito
        model.addAttribute("mensajeExito", "Alumno registrado exitosamente.");
    
        // Mantener la página del formulario
        model.addAttribute("registroAlumno", new RegistroAlumnoDTO()); // Limpiar el formulario
        model.addAttribute("carreras", carreraRepository.findAll()); // Recargar las carreras
        return "/Vistas_Admins_Aptitud/Formulario_Registro_Alumno"; // Regresar al formulario
    }
    

    @GetMapping("/crear-actividad")
    public String mostrarFormularioCrearActividad(Model model) {
        model.addAttribute("actividad", new RegistrarActividadDTO("", "", null, ""));
        return "/Vistas_Admins_Aptitud/Formulario_Registro_Actividad";
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
            return "redirect:/portal/admin/listar-actividades";
        } catch (IllegalArgumentException e) {
            // Manejar errores de validación
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Manejar otros errores
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al crear la actividad.");
        }
    
        // Redirigir a la página de creación en caso de error
        return "redirect:/portal/admin/crear-actividad";
    }



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
        return "/Vistas_Admins_Aptitud/Lista_De_Actividades";
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
        return "redirect:/portal/admin/listar-actividades";
    }

// Método para mostrar el formulario de edición
@GetMapping("/editar/actividad/{id}")
public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
    // Obtener la actividad por ID
    Entidad_ActividadFisica actividad = actividadFisicaServicio.obtenerPorId(id);

    // Convertir a DTO
    ActividadFisicaDTO actividadDTO = actividadFisicaServicio.convertirAActividadFisicaDTO(actividad);

    // Añadir el DTO al modelo
    model.addAttribute("actividadDTO", actividadDTO);
    return "/Vistas_Admins_Aptitud/Editar_Actividad";
}




    // Método para procesar el formulario de edición
    @PostMapping("/editar/actividad/{id}")
    public String editarActividad(@PathVariable Long id, @ModelAttribute RegistrarActividadDTO dto, RedirectAttributes redirectAttributes) {
        actividadFisicaServicio.editarActividad(id, dto);
        redirectAttributes.addFlashAttribute("mensaje", "Actividad actualizada con éxito");
        return "redirect:/portal/admin/listar-actividades";
    }

    @GetMapping("/alumnos-actividad/{id}")
    public String listarAlumnosPorActividad(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<AlumnoDTO> alumnos = actividadFisicaServicio.obtenerAlumnosPorActividad(id);
            model.addAttribute("alumnos", alumnos);
            return "/Vistas_Admins_Aptitud/Lista_De_Alumnos_Por_Act";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo cargar la lista de alumnos: " + e.getMessage());
            return "redirect:/portal/admin/listar-actividades";
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
    
            return "/Vistas_Admins_Aptitud/Mover_Alumno"; // Vista donde se muestra el formulario de actividades
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cargar las opciones de actividades: " + e.getMessage());
            return "redirect:/portal/admin/listar-actividades";
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
        return "redirect:/portal/admin/listar-actividades";
    }

    @GetMapping("/registrar-coach")
    public String mostrarFormularioRegistroCoach(Model model) {
        model.addAttribute("registroCoach", new RegistroCoachDTO());
        return "/Vistas_Admins_Aptitud/Formulario_Registro_Coach"; // Nombre de la vista HTML
    }

    @PostMapping("/registrar-coach")
    public String registrarCoach(RegistroCoachDTO registroCoach, Model model) {
        managerServicio.registrarCoach(registroCoach);
        model.addAttribute("registroCoach", new RegistroCoachDTO());
        return "/Vistas_Admins_Aptitud/Formulario_Registro_Coach";
    }

        @GetMapping("/cambiar-coach/{idActividad}")
    public String mostrarFormularioCambiarCoach(@PathVariable Long idActividad, Model model) {
        Ent_CoachActividad coachActual = coachActividadRepositorio.findByActividadFisicaId(idActividad)
                .orElseThrow(() -> new IllegalArgumentException("No hay un Coach asignado a esta actividad"));
    
        List<CoachDTO> coachesDisponibles = coachActividadServicio.obtenerCoachesDisponibles(coachActual.getUsuario().getIdUsuario());
    
        model.addAttribute("coaches", coachesDisponibles);
        model.addAttribute("idActividad", idActividad);
        return "/Vistas_Admins_Aptitud/Cambiar_Coach";
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
    return "redirect:/portal/admin/editar/" + idActividad; 
}

        // Mostrar formulario para asignar Coach
        @GetMapping("/asignar-coach/{idActividad}")
        public String mostrarFormularioAsignarCoach(@PathVariable Long idActividad, Model model) {
            List<CoachDTO> coaches = coachActividadServicio.obtenerCoaches(); // Obtener todos los coaches
            model.addAttribute("coaches", coaches);
            model.addAttribute("idActividad", idActividad);
            return "/Vistas_Admins_Aptitud/Asignar_Coach"; // Retorna la vista con el formulario
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
            return "redirect:/portal/admin/listar-actividades";
        }

}
