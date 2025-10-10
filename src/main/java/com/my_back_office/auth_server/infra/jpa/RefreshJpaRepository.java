package com.my_back_office.auth_server.infra.jpa;

import com.my_back_office.auth_server.infra.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByMemberId(Long memberId);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
