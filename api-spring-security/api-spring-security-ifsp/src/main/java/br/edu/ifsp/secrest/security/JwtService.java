package br.edu.ifsp.secrest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtService {

    private final String secret;
    private final String issuer;
    private final long expirationMinutes;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.secret = secret;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public String createToken(AuthenticatedUser authenticatedUser) {
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(authenticatedUser.getUsername())
                    .withIssuedAt(now())
                    .withExpiresAt(expiresAt())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new IllegalStateException("Não foi possível gerar o token JWT", exception);
        }
    }

    public String readSubject(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new IllegalArgumentException("Token JWT inválido ou expirado", exception);
        }
    }

    private Instant now() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

    private Instant expiresAt() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .plusMinutes(expirationMinutes)
                .toInstant();
    }
}
