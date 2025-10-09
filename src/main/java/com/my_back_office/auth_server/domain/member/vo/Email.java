package com.my_back_office.auth_server.domain.member.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 이메일을 표현하는 값 객체(VO).
 * record를 사용하여 불변(immutable) 객체로 만듭니다.
 */
public record Email(String value) {

    // RFC 5322 기반의 간단한 이메일 정규식
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    /**
     * 생성자: 객체가 생성되는 시점에 유효성을 검증합니다.
     */
    public Email {
        // 1. null 값 허용 안 함
        Objects.requireNonNull(value, "Email must not be null");

        // 2. 이메일 형식 검증
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}