package com.demo.alb_um.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CambioContrasenaDTO {

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String nuevaContrasena;

    @NotBlank(message = "Debes confirmar la nueva contraseña")
    private String confirmarContrasena;

    // Getters y Setters
    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }
}
