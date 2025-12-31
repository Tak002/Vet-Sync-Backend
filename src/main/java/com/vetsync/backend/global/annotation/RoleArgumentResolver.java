package com.vetsync.backend.global.annotation;

import com.vetsync.backend.global.enums.StaffRole;
import com.vetsync.backend.global.jwt.JwtPrincipalProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RoleArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtPrincipalProvider jwtPrincipalProvider;

    public RoleArgumentResolver(JwtPrincipalProvider jwtPrincipalProvider) {
        this.jwtPrincipalProvider = jwtPrincipalProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Role.class)
                && StaffRole.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return jwtPrincipalProvider.get().role();
    }

}
