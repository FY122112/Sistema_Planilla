/*
package com.todocodeacademy.sistema_planilla.infraestructure.security;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.RoleEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.entity.UsuarioSecEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private JpaUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioSecEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // hash BCrypt de la BD
                .roles(usuario.getRoles().stream()
                        .map(RoleEntity::getName)
                        .toArray(String[]::new))
                .accountExpired(!usuario.isAccountNonExpired())
                .accountLocked(!usuario.isAccountNonLocked())
                .credentialsExpired(!usuario.isCredentialsNonExpired())
                .disabled(!usuario.isEnabled())
                .build();
    }
}
*/
