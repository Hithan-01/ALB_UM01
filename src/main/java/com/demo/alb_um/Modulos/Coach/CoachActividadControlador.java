package com.demo.alb_um.Modulos.Coach;

import com.demo.alb_um.DTOs.CoachDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/coach")
public class CoachActividadControlador {

    @Autowired
    private CoachActividadServicio coachServicio;

    @GetMapping("/{userName}")
    public String obtenerCoachPorUserName(@PathVariable String userName, Model model) {
        Optional<CoachDTO> coachOpt = coachServicio.obtenerCoachPorUserName(userName);
        if (coachOpt.isPresent()) {
            model.addAttribute("coach", coachOpt.get());
            return "coach"; // Nombre de la vista HTML
        }
        return "error"; // Página de error si no se encuentra el coach
    }
}
