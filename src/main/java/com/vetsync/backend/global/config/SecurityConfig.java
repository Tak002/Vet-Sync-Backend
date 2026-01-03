package com.vetsync.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetsync.backend.global.security.JwtAuthFilter;
import com.vetsync.backend.global.security.JwtTokenProvider;
import com.vetsync.backend.global.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/auth/login",
            "/auth/signup",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/management-charts.html",
            "/error",
            "/.well-known/**",
            "/favicon.ico"
    };
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RequestMatcher skipJwtMatcher() {
        PathPatternRequestMatcher.Builder b = PathPatternRequestMatcher.withDefaults();

        List<RequestMatcher> matchers = Arrays.stream(PUBLIC_URLS)
                .map(b::matcher)
                .collect(Collectors.toList());

        return new OrRequestMatcher(matchers);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-minutes}") long minutes
    ) {
        return new JwtTokenProvider(secret, minutes);
    }

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtTokenProvider jwt,RequestMatcher skipJwtMatcher, AuthenticationEntryPoint authenticationEntryPoint) {
        return new JwtAuthFilter(jwt, skipJwtMatcher,authenticationEntryPoint);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



}
