package com.vetsync.backend.global.security;

import com.vetsync.backend.global.enums.StaffRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenMillis;

    public JwtTokenProvider(String secret, long accessTokenMinutes) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret cannot be null or blank");
        }
        if (accessTokenMinutes <= 0) {
            throw new IllegalArgumentException("Access token minutes must be positive");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret is too weak. Provide a longer secret (at least 32 bytes)"
            );
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenMillis = accessTokenMinutes * 60_000L;
    }

    public String createAccessToken(UUID staffId, UUID hospitalId, StaffRole role) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(accessTokenMillis);

        return Jwts.builder()
                .subject(staffId.toString())
                .claim("hospitalId", hospitalId.toString())
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
