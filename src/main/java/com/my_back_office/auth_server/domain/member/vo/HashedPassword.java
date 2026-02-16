package com.my_back_office.auth_server.domain.member.vo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public record HashedPassword(String value) {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static HashedPassword from(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        String hashedPassword = passwordEncoder.encode(plainPassword);
        return new HashedPassword(hashedPassword);
    }

    public boolean matches(String plainPassword) {
        return passwordEncoder.matches(plainPassword, this.value);
    }
}