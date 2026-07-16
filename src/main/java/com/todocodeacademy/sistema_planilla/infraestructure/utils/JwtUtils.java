package com.todocodeacademy.sistema_planilla.infraestructure.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.todocodeacademy.sistema_planilla.infraestructure.security.EmpleadoUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    // =========================
    // 🔑 CREACIÓN DE TOKEN
    // =========================
    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        // Obtener username desde el principal
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        // Authorities como array de strings
        String[] authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withArrayClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2 horas
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()));

        if (authentication.getPrincipal() instanceof EmpleadoUserDetails empleadoUserDetails
                && empleadoUserDetails.getIdEmpleado() != null) {
            builder = builder.withClaim("idEmpleado", empleadoUserDetails.getIdEmpleado());
        }

        return builder.sign(algorithm);
    }

    // =========================
    // 🔒 VALIDACIÓN DE TOKEN
    // =========================
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token, Not Authorized");
        }
    }

    // =========================
    // 🔗 ENLACE PÚBLICO DE BOLETA (HU-060)
    // =========================
    //
    // Token de alcance mínimo (un único claim útil: idBoleta) y vida corta, pensado para
    // pegarlo en un mensaje de WhatsApp que abre alguien sin sesión iniciada. El claim
    // "tipo" evita que un token de este tipo se cuele como si fuera un login normal (no
    // trae "authorities"), y que un JWT de login se reutilice aquí para otra boleta.

    private static final String CLAIM_TIPO = "tipo";
    private static final String TIPO_COMPARTIR_BOLETA = "COMPARTIR_BOLETA";
    private static final long VALIDEZ_COMPARTIR_BOLETA_MS = 72L * 60 * 60 * 1000; // 72 horas

    public String createBoletaShareToken(Long idBoleta) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withClaim(CLAIM_TIPO, TIPO_COMPARTIR_BOLETA)
                .withClaim("idBoleta", idBoleta)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + VALIDEZ_COMPARTIR_BOLETA_MS))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public Long validateBoletaShareToken(String token) {
        DecodedJWT decoded = validateToken(token);

        if (!TIPO_COMPARTIR_BOLETA.equals(decoded.getClaim(CLAIM_TIPO).asString())) {
            throw new JWTVerificationException("El enlace no es válido para esta operación");
        }

        return decoded.getClaim("idBoleta").asLong();
    }

    // =========================
    // 📌 MÉTODOS AUXILIARES
    // =========================
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }
}
