package com.my_back_office.auth_server.infra.jpa;

import com.my_back_office.auth_server.infra.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    // 💡 회원 ID로 토큰을 찾습니다. (로그인 시 토큰 존재 여부 확인 및 갱신에 사용)
    Optional<RefreshTokenEntity> findByMemberId(Long memberId);

    // 💡 토큰 값으로 엔티티를 찾습니다. (토큰 재발급 요청 시 클라이언트의 토큰 검증에 사용)
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
