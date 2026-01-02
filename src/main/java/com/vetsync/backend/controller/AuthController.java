package com.vetsync.backend.controller;

import com.vetsync.backend.dto.auth.LoginRequest;
import com.vetsync.backend.dto.auth.LoginResponse;
import com.vetsync.backend.dto.auth.StaffSignupRequest;
import com.vetsync.backend.dto.auth.StaffSignupResponse;
import com.vetsync.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 / 회원가입 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "직원 로그인",
            description = """
            병원 ID, 로그인 ID, 비밀번호로 로그인합니다.
            
            로그인 성공 시 JWT Access Token을 반환합니다.
            해당 토큰에는 staffId, hospitalId, role 정보가 포함됩니다.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            )
    })
    @SecurityRequirements()
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest req
    ) {
        return ResponseEntity.ok(authService.login(req));
    }


    @PostMapping("/signup")
    @Operation(summary = "직원 회원가입", description = "새로운 직원을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @SecurityRequirements()
    public ResponseEntity<StaffSignupResponse> signup(
            @Valid @RequestBody StaffSignupRequest req
    ) {
        return ResponseEntity.ok(authService.signup(req));
    }
}

