package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.dto.auth.LoginRequest;
import com.vetsync.backend.dto.auth.LoginResponse;
import com.vetsync.backend.dto.auth.StaffSignupRequest;
import com.vetsync.backend.dto.auth.StaffSignupResponse;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.global.security.JwtTokenProvider;
import com.vetsync.backend.global.security.StaffPrincipal;
import com.vetsync.backend.repository.HospitalRepository;
import com.vetsync.backend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StaffUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StaffRepository staffRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        StaffPrincipal principal = (StaffPrincipal) userDetailsService
                .loadByHospitalAndLoginId(req.hospitalId(), req.loginId());

        Staff staff = principal.getStaff();

        if (!passwordEncoder.matches(req.password(), staff.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.createAccessToken(
                staff.getId(),
                staff.getHospital().getId(),
                staff.getRole()
        );

        return new LoginResponse(token);
    }

    @Transactional
    public StaffSignupResponse signup(StaffSignupRequest req) {

        if (staffRepository.existsByHospital_IdAndLoginId(req.hospitalId(), req.loginId())) {
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS);
        }

        Hospital hospital = hospitalRepository.findById(req.hospitalId())
                .orElseThrow(() -> new CustomException(ErrorCode.HOSPITAL_NOT_FOUND));

        Staff staff = Staff.builder()
                .hospital(hospital)
                .loginId(req.loginId())
                .password(passwordEncoder.encode(req.password()))
                .name(req.name())
                .role(req.role())
                .isActive(true)
                .build();

        Staff saved = staffRepository.save(staff);
        return new StaffSignupResponse(saved.getId());
    }
}
