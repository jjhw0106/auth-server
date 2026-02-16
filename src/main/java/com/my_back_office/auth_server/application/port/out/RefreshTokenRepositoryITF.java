package com.my_back_office.auth_server.application.port.out;

import com.my_back_office.auth_server.domain.refreshtoken.RefreshToken;
import com.my_back_office.auth_server.infra.token.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepositoryITF {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshTokenEntity> findByTokenValue(String tokenValue);

    Optional<RefreshTokenEntity> findByMemberId(Long memberId);
}
