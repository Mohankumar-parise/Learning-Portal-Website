package com.portal.authservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface JwtService {

    // Generate a JWT token using username and role
    String generateJwtToken(String username, String role);

    // Validate JWT token and extract the username
    String verifyTokenAndRetrieveUsername(String token);
}

@Service
class JwtServiceImpls implements JwtService {

    private static final String SECRETE_KEY = "My-Practice";
    private static final long EXPIRATION_TIME = 8_64_00_000; // Token validity duration (in ms)

    @Override
    public String generateJwtToken(String username, String role) {
        // Create and sign a new JWT token with subject and role
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRETE_KEY));
    }

    @Override
    public String verifyTokenAndRetrieveUsername(String token) {
        // Verify the token’s signature and extract username
        return JWT.require(Algorithm.HMAC256(SECRETE_KEY))
                .build()
                .verify(token)
                .getSubject();
    }
}
