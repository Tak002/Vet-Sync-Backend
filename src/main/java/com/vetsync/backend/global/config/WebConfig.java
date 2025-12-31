package com.vetsync.backend.global.config;

import com.vetsync.backend.global.annotation.HospitalIdArgumentResolver;
import com.vetsync.backend.global.jwt.JwtPrincipalProvider;
import com.vetsync.backend.global.annotation.RoleArgumentResolver;
import com.vetsync.backend.global.annotation.StaffIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtPrincipalProvider principalProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new StaffIdArgumentResolver(principalProvider));
        resolvers.add(new HospitalIdArgumentResolver(principalProvider));
        resolvers.add(new RoleArgumentResolver(principalProvider));
    }
}
