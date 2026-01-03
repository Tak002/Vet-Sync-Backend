package com.vetsync.backend.global.security;

import com.vetsync.backend.global.ErrorResponse;
import com.vetsync.backend.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException{

        String authError = (String) request.getAttribute("auth_error");

        ErrorCode errorCode;
        if("ACCESS_TOKEN_EXPIRED".equals(authError)) {
            errorCode = ErrorCode.ACCESS_TOKEN_EXPIRED;
        }else{
            errorCode = ErrorCode.INVALID_TOKEN;
        }

        // 여기서 무조건 401로 내리는 걸 추천(토큰 문제는 401)
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = ErrorResponse.of(errorCode);
        objectMapper.writeValue(response.getWriter(), body);

        log.debug("AuthEntryPoint handled: auth_error={}, ex={}", authError, authException.toString());
    }
}
