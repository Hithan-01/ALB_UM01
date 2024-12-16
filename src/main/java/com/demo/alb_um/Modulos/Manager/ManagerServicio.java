package com.demo.alb_um.Modulos.Manager;

import org.springframework.stereotype.Service;

import com.demo.alb_um.DTOs.ManagerDTO;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

@Service
public class ManagerServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public ManagerServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public ManagerDTO obtenerInformacionManagerPorUserName(String userName) {
        Entidad_Usuario usuario = usuarioRepositorio.findByUserName(userName)
            .orElseThrow(() -> new IllegalArgumentException("Manager no encontrado"));

        ManagerDTO manager = new ManagerDTO();
        manager.setNombre(usuario.getNombre());
        manager.setApellido(usuario.getApellido());
        manager.setEmail(usuario.getEmail());
        manager.setFechaIngreso("2020-01-01"); // Este dato puede venir de otra tabla o fuente
        return manager;
    }
}
