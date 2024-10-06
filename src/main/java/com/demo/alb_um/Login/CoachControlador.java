package com.demo.alb_um.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;

import com.demo.alb_um.DTOs.ActividadFisicaDTO;
import com.demo.alb_um.DTOs.PaseListaDTO;
import com.demo.alb_um.Modulos.Coach.CoachActividadServicio;
import com.demo.alb_um.Modulos.Listas.Entidad_Lista;
import com.demo.alb_um.Modulos.Listas.Servicio_lista;


@Controller
@RequestMapping("/portal/coach")
public class CoachControlador {

    private final CoachActividadServicio coachActividadServicio;
    private final Servicio_lista listaServicio;
    
    

    @Autowired
    @Lazy
    public CoachControlador(CoachActividadServicio coachActividadServicio, 
                            Servicio_lista listaServicio) {
        this.coachActividadServicio = coachActividadServicio;
        this.listaServicio = listaServicio;
       
    }


    @GetMapping("/listadealumno/{idActividadFisica}")
    public String mostrarListaAlumnos(@PathVariable Long idActividadFisica, Model model) {
        ActividadFisicaDTO actividad = coachActividadServicio.obtenerActividadPorId(idActividadFisica);
        model.addAttribute("actividad", actividad);
        return "listaAlumnos"; 
    }

  
    @PostMapping("/paseLista/{idActividadFisica}")
    public String iniciarPaseLista(@PathVariable Long idActividadFisica, Model model) {
        PaseListaDTO paseListaInfo = coachActividadServicio.iniciarPaseLista(idActividadFisica);
        
        model.addAttribute("lista", paseListaInfo.getLista());
        model.addAttribute("actividad", paseListaInfo.getActividad());
        model.addAttribute("alumnos", paseListaInfo.getAlumnos());

        return "paseLista"; 
    }
    
    @PostMapping("/guardarAsistencia/{idLista}")
    public String guardarAsistencia(@PathVariable Long idLista, @RequestParam("asistencias") List<Long> asistencias) {
       
        Entidad_Lista lista = listaServicio.obtenerListaPorId(idLista);

        LocalDateTime horaActual = LocalDateTime.now();
        listaServicio.guardarAsistencia(lista, asistencias, horaActual);

        return "redirect:/portal/inicio";
    }
}