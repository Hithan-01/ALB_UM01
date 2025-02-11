package com.demo.alb_um.Config;

import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ForzarCambioContrasenaFilter extends OncePerRequestFilter {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ForzarCambioContrasenaFilter(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            filterChain.doFilter(request, response);
            return;
        }
    
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
    
        // Obtener el contexto del servidor para adaptarse a `/Alb_Um`
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath(); // Esto será `/Alb_Um`
    
        // Permitir acceso a cambio de contraseña y logout
        if (requestURI.equals(contextPath + "/alumno/cambiar-contrasena") || requestURI.equals(contextPath + "/logout")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        // Buscar usuario en la base de datos
        Entidad_Usuario usuario = usuarioRepositorio.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        // Si la contraseña sigue siendo la matrícula, redirigir al cambio de contraseña
        if (passwordEncoder.matches(username, usuario.getContrasena())) {
            response.sendRedirect(contextPath + "/alumno/cambiar-contrasena");
            return;
        }
    
        // Continuar con la petición si la contraseña ya fue cambiada
        filterChain.doFilter(request, response);
    }
    
    

}
