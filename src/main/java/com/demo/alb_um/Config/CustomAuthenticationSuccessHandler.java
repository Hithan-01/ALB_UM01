package com.demo.alb_um.Config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User has no roles assigned"))
                .getAuthority()
                .replace("ROLE_", "").toLowerCase(); // Elimina el prefijo y convierte a minúsculas

        switch (role) {
            case "admin":
                response.sendRedirect("/admin/" + authentication.getName());
                break;
            case "alumno":
                response.sendRedirect("/alumno/" + authentication.getName());
                break;
            case "coach":
                response.sendRedirect("/coach/" + authentication.getName());
                break;
            default:
                response.sendRedirect("/error");
                break;
        }
    }

    
}
