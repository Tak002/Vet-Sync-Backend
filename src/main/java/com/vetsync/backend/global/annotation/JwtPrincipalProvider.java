package com.vetsync.backend.global.annotation;

import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.global.jwt.JwtPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtPrincipalProvider {

    public JwtPrincipal get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof JwtPrincipal jwtPrincipal)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return jwtPrincipal;
    }
}
