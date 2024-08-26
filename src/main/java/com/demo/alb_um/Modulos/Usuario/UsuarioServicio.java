package com.demo.alb_um.Modulos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    public List<Entidad_Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}
