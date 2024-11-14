package com.demo.alb_um.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import java.util.List;

@Component
public class PasswordEncryptionInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        List<Entidad_Usuario> usuarios = usuarioRepository.findByContrasenaNotLike("$2a$%");
        
        for (Entidad_Usuario usuario : usuarios) {
            String encryptedPassword = passwordEncoder.encode(usuario.getContrasena());
            usuario.setContrasena(encryptedPassword);
            usuarioRepository.save(usuario);
            System.out.println("Contraseña encriptada para el usuario: " + usuario.getUserName());
        }
    }
}