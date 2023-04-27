package edu.ukma.zlagoda.entities;

import org.springframework.security.core.GrantedAuthority;

public enum EmployeeRole implements GrantedAuthority {
    CASHIER,
    MANAGER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
