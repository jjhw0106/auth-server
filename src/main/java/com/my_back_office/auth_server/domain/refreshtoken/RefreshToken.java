package com.my_back_office.auth_server.domain.refreshtoken;

import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class RefreshToken {

    // Refresh Token의 도메인 식별자
    private final Long id;

    // 이 토큰이 누구의 것인지 식별하는 ID
    private final Long memberId;

    // Refresh Token의 실제 문자열 값
    private final String refreshToken;

    // 생성 시간
    private final Date createdAt;

    @Builder
    public RefreshToken(Long id, Long memberId, String tokenValue, Date createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.refreshToken = tokenValue;
        this.createdAt = createdAt;
    }

    // --- 도메인 로직 ---

    /**
     * Refresh Token의 만료 여부를 확인하는 도메인 로직 (예시)
     * 실제 만료 시간은 JWT Claims에 있으므로, 이 로직은 DB 저장된 토큰의 유효성을 체크하는 로직으로 활용될 수 있음.
     */
    public boolean isExpired(long validDurationMilliseconds) {
        if (createdAt == null) {
            return true;
        }
        // 생성 시간 + 유효기간이 현재 시간보다 이전인지 확인
        Instant expirationTime = createdAt.toInstant().plusMillis(validDurationMilliseconds);
        return expirationTime.isBefore(Instant.now());
    }

    public static RefreshToken createNew(String tokenValue, Long memberId) {
        return RefreshToken.builder()
            .memberId(memberId)
            .tokenValue(tokenValue)
            .createdAt(new Date())
            .build();
    }

    public static RefreshToken fromPersistence(
        Long id,
        Long memberId,
        String refreshToken,
        Date createdAt
    ) {
        return new RefreshToken(
            id,
            memberId,
            refreshToken,
            createdAt
        );
    }
}