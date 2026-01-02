package com.vetsync.backend.global.security;

import com.vetsync.backend.global.enums.StaffRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;
    private final RequestMatcher skipMatcher;

    public JwtAuthFilter(JwtTokenProvider jwt, RequestMatcher skipMatcher) {
        this.jwt = jwt;
        this.skipMatcher = skipMatcher;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwt.parseAndValidate(token);

            UUID staffId = UUID.fromString(claims.getSubject());
            UUID hospitalId = UUID.fromString(String.valueOf(claims.get("hospitalId")));
            StaffRole role = StaffRole.valueOf(String.valueOf(claims.get("role")));

            JwtPrincipal principal = new JwtPrincipal(staffId, hospitalId, role);

            var authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e){
            // ACCESS_TOKEN_EXPIRED
            request.setAttribute("auth_error", "ACCESS_TOKEN_EXPIRED");
            SecurityContextHolder.clearContext();
            throw new org.springframework.security.core.AuthenticationException("expired") {};
        }
        catch (Exception e) {
            // INVALID_TOKEN
            log.warn("JWT validation failed: {}", e.toString());
            request.setAttribute("auth_error", "INVALID_TOKEN");
            SecurityContextHolder.clearContext();
            throw new org.springframework.security.core.AuthenticationException("invalidate_token") {};
        }
        chain.doFilter(request, response);
    }
}
