package com.demo.alb_um.Config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private final Entidad_Usuario usuario;

    public UserPrincipal(Entidad_Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Añade el prefijo "ROLE_" al rol almacenado en la base de datos
        String roleWithPrefix = "ROLE_" + usuario.getRol().toUpperCase(); // Convierte a mayúsculas para consistencia
        return Collections.singleton(new SimpleGrantedAuthority(roleWithPrefix));
    }

    @Override
    public String getPassword() {
        return usuario.getContrasena();
    }

    @Override
    public String getUsername() {
        return usuario.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Entidad_Usuario getUsuario() {
        return usuario;
    }

    public static UserPrincipal create(Entidad_Usuario usuario) {
        return new UserPrincipal(usuario);
    }
}
