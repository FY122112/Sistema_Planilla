package com.todocodeacademy.sistema_planilla.infraestructure.input.controllers;

import com.todocodeacademy.sistema_planilla.infraestructure.security.UserDetailsServiceImp;
import com.todocodeacademy.sistema_planilla.infraestructure.security.dto.AuthLoginRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.security.dto.AuthResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {

        //return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);

        return ResponseEntity.ok(
                this.userDetailsService.loginUser(userRequest)
        );

    }
}