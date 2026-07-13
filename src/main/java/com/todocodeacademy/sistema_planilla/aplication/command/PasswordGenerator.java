package com.todocodeacademy.sistema_planilla.aplication.command;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456"; // la contraseña en texto plano
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}