package com.vetsync.backend.global.jwt;

import com.vetsync.backend.global.enums.StaffRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenMillis;

    public JwtTokenProvider(String secretBase64, long accessTokenMinutes) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64)); // :contentReference[oaicite:2]{index=2}
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
