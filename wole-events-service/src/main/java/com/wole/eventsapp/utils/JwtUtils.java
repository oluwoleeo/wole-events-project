package com.wole.eventsapp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.valid-for}")
    private Integer tokenValidity;

    @Value("${jwt.issuer}")
    private String tokenIssuer;

    public String generateToken(String email){
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);

        return JWT.create()
                .withIssuer(tokenIssuer)
                .withSubject(email)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(tokenValidity))
                .sign(algorithm);
    }

    public String verifyToken(String tokenToVerify){
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);

        return JWT.require(algorithm)
                .build()
                .verify(tokenToVerify)
                .getSubject();
    }
}
