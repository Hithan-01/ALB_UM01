package com.demo.alb_um.Modulos.Admn;

import com.demo.alb_um.DTOs.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class UsuarioAdminControlador {

    @Autowired
    private UsuarioAdminServicio usuarioAdminServicio;

    @GetMapping("/{userName}")
    public String obtenerInformacionAdminPorUserName(@PathVariable String userName, Model model) {
        Optional<AdminDTO> adminOpt = usuarioAdminServicio.obtenerInformacionAdminPorUserName(userName);
        if (adminOpt.isPresent()) {
            AdminDTO admin = adminOpt.get();
            model.addAttribute("admin", admin);
            return "admin";
        }
        return "error"; // Página de error si no se encuentra el administrador
    }
}
