package com.svart.SVart.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, User;


    @Override
    public String getAuthority() {
        return name();
    }
}
