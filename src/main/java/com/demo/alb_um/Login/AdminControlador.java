package com.demo.alb_um.Login;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;

import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaRepositorio;
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
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;
import com.demo.alb_um.Modulos.Citas.CitaServicio;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Inscripcion_Taller.Ent_InscripcionTaller;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerRepositorio;
import com.demo.alb_um.Modulos.Inscripcion_Taller.InscripcionTallerServicio;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;
import com.demo.alb_um.DTOs.AlumnoDTO;


@Controller
@RequestMapping("/portal/admin")
public class AdminControlador {

    private final UsuarioAlumnoServicio usuarioAlumnoServicio;
    private final CitaServicio citaServicio;
    private final UsuarioAdminRepositorio usuarioAdminRepositorio;
    private final InscripcionTallerServicio inscripcionTallerServicio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final AntropometriaRepositorio antropometriaRepositorio;
    private final AntropometriaServicio antropometriaServicio;
    private final CitaRepositorio citaRepositorio;
    private final UsuarioAlumnoRepositorio usuarioAlumnoRepositorio;
    private final ActividadFisicaRepositorio actividadFisicaRepositorio;
    private final RepositorioAsistenciaActividadFisica asistenciaActividadFisicaRepositorio;
    @Autowired
    public AdminControlador(UsuarioAlumnoServicio usuarioAlumnoServicio, 
                            CitaServicio citaServicio, 
                            UsuarioAdminRepositorio usuarioAdminRepositorio, 
                            InscripcionTallerServicio inscripcionTallerServicio, 
                            InscripcionTallerRepositorio inscripcionTallerRepositorio,
                            AntropometriaRepositorio antropometriaRepositorio,
                            AntropometriaServicio antropometriaServicio,
                            CitaRepositorio citaRepositorio,
                            UsuarioRepositorio usuarioRepositorio,
                            UsuarioAlumnoRepositorio usuarioAlumnoRepositorio,
                            ActividadFisicaRepositorio actividadFisicaRepositorio,
                            RepositorioAsistenciaActividadFisica asistenciaActividadFisicaRepositorio) {
        this.usuarioAlumnoServicio = usuarioAlumnoServicio;
        this.citaServicio = citaServicio;
        this.usuarioAdminRepositorio = usuarioAdminRepositorio;
        this.inscripcionTallerServicio = inscripcionTallerServicio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.antropometriaRepositorio = antropometriaRepositorio;
        this.antropometriaServicio = antropometriaServicio;
        this.citaRepositorio = citaRepositorio;
        this.usuarioAlumnoRepositorio = usuarioAlumnoRepositorio;
        this.actividadFisicaRepositorio = actividadFisicaRepositorio;
        this.asistenciaActividadFisicaRepositorio = asistenciaActividadFisicaRepositorio;
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
        return "redirect:/portal/admin";
    }

    model.addAttribute("taller", new Ent_Taller());
    return "crearTaller"; // Vista Thymeleaf para crear talleres
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
        return "redirect:/portal/admin";
    }

    List<Ent_Taller> talleres = inscripcionTallerServicio.obtenerTodosTalleres();
    model.addAttribute("talleres", talleres);
    return "listaTalleres"; // Vista Thymeleaf para mostrar talleres
}

@GetMapping("/talleres/{id}/detalles")
public String verDetallesTaller(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Ent_Taller> tallerOpt = inscripcionTallerServicio.obtenerTallerPorId(id);
    if (tallerOpt.isPresent()) {
        model.addAttribute("taller", tallerOpt.get());
        return "detallesTaller";
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
    return "editarTaller"; // Vista para editar talleres
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
        return "admin"; // Regresa a la página de administrador con el mensaje de error
    }

    @GetMapping("/detalle-alumno/{id}")
    public String mostrarDetalleAlumno(@PathVariable Long id, Model model) {
        // Obtener información del alumno
        Optional<Entidad_Usuario_Alumno> alumnoOpt = usuarioAlumnoRepositorio.findById(id);
    
        if (alumnoOpt.isPresent()) {
            Entidad_Usuario_Alumno alumno = alumnoOpt.get();
            model.addAttribute("alumno", alumno);
    
            // Obtener las actividades físicas relacionadas y calcular asistencias/faltas/justificaciones
            List<Map<String, Object>> actividades = alumno.getAlumnoActividades().stream().map(relacion -> {
                Entidad_ActividadFisica actividad = relacion.getActividadFisica();
    
                // Contar asistencias, faltas y justificadas
                long asistencias = actividad.getAlumnoActividades().stream()
                    .filter(a -> a.getUsuarioAlumno().getIdUsuarioAlumno().equals(alumno.getIdUsuarioAlumno()))
                    .flatMap(a -> a.getUsuarioAlumno().getAsistencias().stream())
                    .filter(a -> a.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.PRESENTE)
                    .count();
    
                long faltas = actividad.getAlumnoActividades().stream()
                    .filter(a -> a.getUsuarioAlumno().getIdUsuarioAlumno().equals(alumno.getIdUsuarioAlumno()))
                    .flatMap(a -> a.getUsuarioAlumno().getAsistencias().stream())
                    .filter(a -> a.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.FALTA)
                    .count();
    
                long justificadas = actividad.getAlumnoActividades().stream()
                    .filter(a -> a.getUsuarioAlumno().getIdUsuarioAlumno().equals(alumno.getIdUsuarioAlumno()))
                    .flatMap(a -> a.getUsuarioAlumno().getAsistencias().stream())
                    .filter(a -> a.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA)
                    .count();

                    
    
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
            return "detalleAlumno";
        } else {
            model.addAttribute("error", "No se encontró al alumno.");
            return "admin";
        }

        
    }
    
    
    @GetMapping("/ver-detalles-asistencias/{idActividad}/{idAlumno}")
    public String verDetallesAsistencias(@PathVariable Long idActividad, @PathVariable Long idAlumno, Model model) {
        Optional<Entidad_ActividadFisica> actividadOpt = actividadFisicaRepositorio.findById(idActividad);
    
        if (actividadOpt.isPresent()) {
            Entidad_ActividadFisica actividad = actividadOpt.get();
    
            // Obtener asistencias con lógica consistente
            List<Map<String, Object>> asistencias = actividad.getAlumnoActividades().stream()
                .filter(relacion -> relacion.getUsuarioAlumno().getIdUsuarioAlumno().equals(idAlumno))
                .flatMap(relacion -> relacion.getUsuarioAlumno().getAsistencias().stream()
                    .filter(asistencia -> asistencia.getLista() != null 
                        && asistencia.getLista().getActividadFisica().getIdActividadFisica().equals(idActividad)))
                .map(asistencia -> {
                    // Crear un mapa con la información detallada de cada asistencia
                    Map<String, Object> asistenciaInfo = new HashMap<>();
                    asistenciaInfo.put("fechaRegistro", asistencia.getFechaRegistro());
                    asistenciaInfo.put("estadoFalta", asistencia.getEstadoFalta() != null ? asistencia.getEstadoFalta().name() : "Sin estado");
                    asistenciaInfo.put("idAsistenciaActividadFisica", asistencia.getIdAsistenciaActividadFisica());
                    return asistenciaInfo;
                })
                .collect(Collectors.toList());
    
            model.addAttribute("actividad", actividad);
            model.addAttribute("asistencias", asistencias);
    
            return "detallesAsistencias";
        } else {
            model.addAttribute("error", "No se encontraron detalles para esta actividad.");
            return "detalleAlumno";
        }
    }
    
    

    @PostMapping("/justificar-asistencia/{id}")
    public String justificarAsistencia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Ent_AsistenciaActividadFisica> asistenciaOpt = asistenciaActividadFisicaRepositorio.findById(id);
    
        if (asistenciaOpt.isPresent()) {
            Ent_AsistenciaActividadFisica asistencia = asistenciaOpt.get();
    
            // Verificamos que la asistencia pueda ser justificada (solo si está en estado FALTA)
            if (asistencia.getEstadoFalta() == Ent_AsistenciaActividadFisica.EstadoFalta.FALTA) {
                asistencia.setEstadoFalta(Ent_AsistenciaActividadFisica.EstadoFalta.JUSTIFICADA);
                asistenciaActividadFisicaRepositorio.save(asistencia);
                redirectAttributes.addFlashAttribute("mensaje", "Falta justificada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Solo se pueden justificar asistencias en estado 'FALTA'.");
            }
    
            // Redirigir a la página de detalles de asistencias del alumno y actividad
            Long idActividad = asistencia.getLista().getActividadFisica().getIdActividadFisica();
            Long idAlumno = asistencia.getUsuarioAlumno().getIdUsuarioAlumno();
            return "redirect:/portal/admin/ver-detalles-asistencias/" + idActividad + "/" + idAlumno;
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la asistencia para justificar.");
            return "redirect:/portal/admin/detalleAlumno"; // Cambia a la página general si no se encuentra la asistencia
        }
    }
    
    

    
}
