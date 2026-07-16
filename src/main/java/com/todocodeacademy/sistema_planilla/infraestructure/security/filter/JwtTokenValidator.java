package com.todocodeacademy.sistema_planilla.infraestructure.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.todocodeacademy.sistema_planilla.infraestructure.security.AuthenticatedPrincipal;
import com.todocodeacademy.sistema_planilla.infraestructure.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            try {
                // Validar y decodificar el token
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                // Extraer username y authorities
                String username = jwtUtils.extractUsername(decodedJWT);

                // El claim se firma como arreglo JSON (JwtUtils.createToken usa
                // withArrayClaim), así que hay que leerlo como arreglo. Claim.asString()
                // devuelve null para un claim que no es un string plano: con eso, todo
                // usuario autenticado terminaba con cero authorities sin importar sus
                // roles reales, algo invisible mientras nada dependía de hasRole(...).
                String[] authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asArray(String.class);

                Collection<? extends GrantedAuthority> authoritiesList =
                        AuthorityUtils.createAuthorityList(authorities != null ? authorities : new String[0]);

                // El claim es opcional: una cuenta ADMINISTRADOR sin empleado vinculado no
                // lo trae, y decodedJWT.getClaim(...) devuelve un Claim "vacío" (no null) en
                // ese caso, cuyo asLong() sí devuelve null de forma segura.
                Long idEmpleado = jwtUtils.getSpecificClaim(decodedJWT, "idEmpleado").asLong();

                // Solo setear Authentication si aún no existe en el contexto
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    new AuthenticatedPrincipal(username, idEmpleado),
                                    null,
                                    authoritiesList
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException exception) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(
                        "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Token inválido o expirado\"}"
                );
                return;
            }
        }

        // Continuar con el resto de filtros
        filterChain.doFilter(request, response);
    }
}
