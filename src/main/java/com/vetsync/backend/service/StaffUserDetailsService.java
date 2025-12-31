package com.vetsync.backend.service;

import com.vetsync.backend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.vetsync.backend.global.security.StaffPrincipal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffUserDetailsService {

    private final StaffRepository staffRepository;

    public UserDetails loadByHospitalAndLoginId(UUID hospitalId, String loginId) {
        return staffRepository.findByHospital_IdAndLoginId(hospitalId, loginId)
                .map(StaffPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("staff not found"));
    }
}
