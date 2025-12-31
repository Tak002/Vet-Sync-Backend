package com.vetsync.backend.service;

import com.vetsync.backend.dto.owner.OwnerInfoResponse;
import com.vetsync.backend.dto.owner.OwnerRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    public OwnerInfoResponse registerOwner(@Valid OwnerRegisterRequest ownerRegisterRequest) {
        return null;
    }
}
