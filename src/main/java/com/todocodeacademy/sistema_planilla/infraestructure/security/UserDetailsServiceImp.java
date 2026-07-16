package com.todocodeacademy.sistema_planilla.infraestructure.security;

import com.todocodeacademy.sistema_planilla.infraestructure.entity.UsuarioSecEntity;
import com.todocodeacademy.sistema_planilla.infraestructure.repository.JpaUsuarioRepository;
import com.todocodeacademy.sistema_planilla.infraestructure.security.dto.AuthLoginRequestDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.security.dto.AuthResponseDTO;
import com.todocodeacademy.sistema_planilla.infraestructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private final JpaUsuarioRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioSecEntity userSec = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no fue encontrado"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Roles
        userSec.getRoles().forEach(rol ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getName()))
        );

        // Permisos
        userSec.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission ->
                        authorities.add(new SimpleGrantedAuthority(permission.getName()))
                );

        Long idEmpleado = userSec.getEmpleado() != null
                ? userSec.getEmpleado().getIdEmpleado()
                : null;

        return new EmpleadoUserDetails(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNonExpired(),
                userSec.isCredentialsNonExpired(),
                userSec.isAccountNonLocked(),
                authorities,
                idEmpleado
        );
    }

    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequest){

        //recuperamos el nombre de usuario y contraseña

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username,"Login successfull", accessToken,true);
        return authResponseDTO;
    }

    public Authentication authenticate(String username, String password){

        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        //return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(
                userDetails,   // 🔥 AQUÍ ESTÁ LA CLAVE
                null,
                userDetails.getAuthorities()
        );
    }


}
