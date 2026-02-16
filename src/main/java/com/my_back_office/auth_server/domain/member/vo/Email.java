package com.my_back_office.auth_server.domain.member.vo;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public Email {
        Objects.requireNonNull(value, "Email must not be null");

        // 이메일 형식 검증
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}