package com.my_back_office.auth_server.domain.member.vo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 해시된 비밀번호를 표현하는 값 객체(VO).
 * 비밀번호 해싱 및 검증 책임을 스스로 가집니다.
 */
public record HashedPassword(String value) {

    // Spring Security의 Bcrypt 구현체를 사용하는 것이 표준적입니다.
    // 실제로는 Bean으로 주입받아 사용하는 것이 좋습니다.
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 평문 비밀번호로부터 HashedPassword 객체를 생성합니다. (회원가입 시 사용)
     *
     * @param plainPassword 암호화되지 않은 비밀번호
     * @return HashedPassword 인스턴스
     */
    public static HashedPassword from(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        // 내부적으로 비밀번호를 해싱합니다.
        String hashedPassword = passwordEncoder.encode(plainPassword);
        return new HashedPassword(hashedPassword);
    }

    /**
     * 입력된 평문 비밀번호가 저장된 해시와 일치하는지 확인합니다. (로그인 시 사용)
     *
     * @param plainPassword 사용자가 입력한 비밀번호
     * @return 일치 여부
     */
    public boolean matches(String plainPassword) {
        return passwordEncoder.matches(plainPassword, this.value);
    }
}