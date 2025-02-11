package com.demo.alb_um.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio; // Repositorio para obtener emails de usuarios

    /**
     * Envía un correo individual a un destinatario específico.
     */
    public void enviarCorreoIndividual(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(destinatario);
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);
        mailSender.send(mailMessage);
    }

    /**
     * Envía un correo masivo a todos los alumnos registrados en la base de datos.
     */
    public void enviarCorreoMasivo(String asunto, String mensaje) {
        List<String> emails = usuarioRepositorio.obtenerEmailsDeAlumnos(); // Obtener solo alumnos
        for (String email : emails) {
            enviarCorreoIndividual(email, asunto, mensaje);
        }
    }
}
