package com.demo.alb_um.Login;



import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Aquí puedes devolver el nombre de la vista personalizada
        return "error"; // Nombre del archivo HTML en la carpeta templates, como error.html
    }
}
