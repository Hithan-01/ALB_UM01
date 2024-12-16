package com.demo.alb_um.Login;

import com.demo.alb_um.DTOs.CoachDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;

import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaServicio;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.util.List;

@Controller
@RequestMapping("/portal/manager")
public class ManagerControlador {

    @Autowired
    private CoachActividadServicio coachServicio;


    @Autowired
    private ActividadFisicaServicio actividadFisicaServicio;

    @GetMapping("/crear-actividad")
    public String mostrarFormularioCrearActividad(Model model) {
        // Obtener la lista de coaches
        List<CoachDTO> coaches = coachServicio.obtenerCoaches();
        System.out.println("Coaches obtenidos: " + coaches);

        // Agregar atributos al modelo
        model.addAttribute("actividad", new Entidad_ActividadFisica());
        model.addAttribute("coaches", coaches);

        return "crearActividad";
    }
   

    @PostMapping("/crear-actividad")
public String crearActividad(@RequestParam("nombre") String nombre,
                             @RequestParam("diaSemana") String diaSemana,
                             @RequestParam("hora") String hora,
                             @RequestParam("grupo") String grupo,
                             @RequestParam("coachId") Integer coachId,
                             RedirectAttributes redirectAttributes) {
    try {
        // Validación del servidor
        if (nombre == null || !nombre.matches("[A-Za-záéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }
        if (grupo == null || !grupo.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("El grupo solo puede contener letras y números.");
        }

        Time horaTime = Time.valueOf(hora + ":00"); // Convierte la hora

        // Servicio
        actividadFisicaServicio.crearActividadFisica(nombre, diaSemana, horaTime, grupo, coachId);

        // Mensaje de éxito
        redirectAttributes.addFlashAttribute("mensajeExito", "Actividad creada exitosamente.");
        return "redirect:/portal/inicio";
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", "Error de validación: " + e.getMessage());
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error inesperado al crear la actividad.");
    }
    return "redirect:/portal/manager/crear-actividad";
}


}
