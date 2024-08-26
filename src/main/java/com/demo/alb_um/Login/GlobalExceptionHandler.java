package com.demo.alb_um.Login;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, RedirectAttributes redirectAttributes) {
        // Aquí puedes registrar la excepción o enviar información a la vista
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // Nombre de la vista de error personalizada
    }
}
