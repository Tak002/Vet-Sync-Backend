package com.vetsync.backend.global.jwt;

import com.vetsync.backend.global.enums.StaffRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;

    public JwtAuthFilter(JwtTokenProvider jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            Claims claims = jwt.parseAndValidate(token);

            UUID staffId = UUID.fromString(claims.getSubject());
            UUID hospitalId = UUID.fromString((String) claims.get("hospitalId"));
            StaffRole role = StaffRole.valueOf((String) claims.get("role"));

            // principal에 우리가 원하는 값을 담는 "세션 없는" 방식
            var principal = new JwtPrincipal(staffId, hospitalId, role);

            var authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role.name())) //아직 사용 하지는 않음
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            SecurityContextHolder.clearContext(); // 토큰 이상하면 비인증으로
        }

        chain.doFilter(request, response);
    }

    public record JwtPrincipal(UUID staffId, UUID hospitalId, StaffRole role) {}
}
