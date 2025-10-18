package com.my_back_office.auth_server.domain.member;

import com.my_back_office.auth_server.domain.member.vo.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private final Long memberId; // DB에서 생성된 후 할당되는 ID
    private final Email email;
    private HashedPassword password;
    private String nickname;
    private Role role;
    private Status status;
    private final Provider provider;
    private final String providerId; // 소셜 가입 시에만 존재
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 로컬(자체) 회원가입을 위한 생성 메서드
     */
    public static Member localRegister(Email email, String plainPassword, String nickname) {
        HashedPassword hashedPassword = HashedPassword.from(plainPassword);
        return new Member(
            null, // memberId는 DB에 저장될 때 생성되므로 처음엔 null
            email,
            hashedPassword,
            nickname,
            Role.USER,
            Status.ACTIVE,
            Provider.LOCAL,
           null, // 로컬 가입이므로 providerId는 null
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Member fromPersistence(
        Long memberId,
        Email email,
        HashedPassword password,
        String nickname,
        String role,
        String status,
        String provider,
        String providerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        return new Member(
            memberId,
            email,
            password,
            nickname,
            Role.valueOf(role),
            Status.valueOf(status),
            Provider.valueOf(provider),
            providerId,
            createdAt,
            updatedAt
        );
    }

    public static Member socialRegister(Email email, String nickname, Provider provider, String providerId) {
        if (provider == Provider.LOCAL) {
            throw new IllegalArgumentException("Social register cannot be LOCAL provider.");
        }
        return new Member(
            null,
            email,
            null, // 소셜 가입이므로 비밀번호는 null
            nickname,
            Role.USER,
            Status.ACTIVE,
            provider,
            providerId,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public void authenticate(String plainPassword) {
        if (this.provider != Provider.LOCAL) {
            throw new IllegalStateException("Only LOCAL members can authenticate with password.");
        }
        if (!this.password.matches(plainPassword)) {
            throw new IllegalArgumentException("Password does not match.");
        }
    }
    public void changeNickname(String newNickname) {
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be empty.");
        }
        this.nickname = newNickname;
        this.updatedAt = LocalDateTime.now();
    }

    public void ban() {
        this.status = Status.BANNED;
        this.updatedAt = LocalDateTime.now();
    }


}

