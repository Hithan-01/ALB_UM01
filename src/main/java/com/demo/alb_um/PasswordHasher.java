package com.demo.alb_um;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Scanner;

public class PasswordHasher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContraseña = scanner.nextLine().trim(); // Elimina espacios en blanco accidentales

        if (nuevaContraseña.isEmpty()) {
            System.out.println("La contraseña no puede estar vacía.");
        } else {
            String hashedPassword = passwordEncoder.encode(nuevaContraseña);
            System.out.println("Contraseña encriptada: " + hashedPassword);
        }

        scanner.close();
    }
}
