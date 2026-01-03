package com.vetsync.backend.global.security;

import com.vetsync.backend.domain.Staff;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class StaffPrincipal extends User {
    private final Staff staff;

    public StaffPrincipal(Staff staff) {
        super(
                staff.getLoginId(),
                staff.getPassword(),
                staff.isActive(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name()))
        );
        this.staff = staff;
    }

    public Staff getStaff() { return staff; }
}
