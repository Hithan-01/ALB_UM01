package com.demo.alb_um.Login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favicon.ico")
public class FaviconController {
    @GetMapping
    public void handleFavicon() {
        // No hacer nada, solo evitar el warning en la consola
    }
}
