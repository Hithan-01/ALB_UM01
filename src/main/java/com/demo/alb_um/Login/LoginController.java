package com.demo.alb_um.Login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        // Aquí puedes agregar lógica para mostrar diferente contenido basado en el rol del usuario
        return "home";
    }
}
