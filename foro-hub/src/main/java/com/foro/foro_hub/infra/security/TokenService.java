package com.foro.foro_hub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.foro.foro_hub.domain.usuario.Usuario;
import com.foro.foro_hub.infra.errores.TokenInvalidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("foro-hub")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (Exception exception) {
            throw new RuntimeException("Error al generar token JWT: " + exception.getMessage());
        }
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new TokenInvalidoException("Token nulo");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("foro-hub")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenInvalidoException("Token JWT inválido: " + exception.getMessage());
        }
    }

    // Fix #6: sin offset hardcodeado — usa Instant.now() que es siempre UTC
    private Instant generarFechaExpiracion() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}
