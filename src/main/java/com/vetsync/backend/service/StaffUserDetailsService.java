package com.vetsync.backend.service;

import com.vetsync.backend.global.security.StaffPrincipal;
import com.vetsync.backend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffUserDetailsService {

    private final StaffRepository staffRepository;

    @Transactional(readOnly = true)
    public UserDetails loadByHospitalAndLoginId(UUID hospitalId, String loginId) {
        return staffRepository.findByHospital_IdAndLoginId(hospitalId, loginId)
                .map(StaffPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("staff not found"));
    }
}
