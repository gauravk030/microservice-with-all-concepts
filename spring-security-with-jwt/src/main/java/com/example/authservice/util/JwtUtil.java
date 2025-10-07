package com.example.authservice.util;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@RefreshScope
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 10; // 10 hours
    private static final long CLOCK_SKEW_SEC = 60; // allow 1 minute clock skew

    // Extract username (subject) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim using a resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse all claims from the token with clock skew
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey())
                   .setAllowedClockSkewSeconds(CLOCK_SKEW_SEC)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    // Generate JWT token
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + JWT_EXPIRATION_MS))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    // Get the signing key from the base64-encoded secret
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        if (keyBytes.length < 32) { // HS256 requires at least 32 bytes
            throw new IllegalArgumentException(
                "JWT secret key is too short. Must be at least 256 bits / 32 bytes.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate token against username
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
