package com.vetsync.backend.global.jwt;

import com.vetsync.backend.global.enums.StaffRole;

import java.util.UUID;

public record JwtPrincipal(UUID staffId, UUID hospitalId, StaffRole role) {}
