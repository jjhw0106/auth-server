package com.my_back_office.auth_server.infra.token;

import com.my_back_office.auth_server.application.port.out.RefreshTokenRepositoryITF;
import com.my_back_office.auth_server.domain.refreshtoken.RefreshToken;
import com.my_back_office.auth_server.infra.jpa.RefreshJpaRepository;
import com.my_back_office.auth_server.infra.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository implements RefreshTokenRepositoryITF {

    private final RefreshJpaRepository repository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entityToSave = RefreshTokenEntity.fromDomain(refreshToken);

        // DB에서 memberId를 기준으로 기존 토큰을 찾습니다.
        Optional<RefreshTokenEntity> existingTokenOpt = repository.findByMemberId(refreshToken.getMemberId());

        if (existingTokenOpt.isPresent()) {
            // 기존 토큰이 있으면, 그 토큰의 ID를 그대로 사용하고 값만 갱신합니다.
            RefreshTokenEntity existingEntity = existingTokenOpt.get();
            existingEntity.updateTokenValue(entityToSave.getRefreshToken());

            // save를 호출하면 ID가 있으므로 UPDATE 쿼리가 실행됩니다.
            RefreshTokenEntity updatedEntity = repository.save(existingEntity);
            return updatedEntity.toDomain();
        } else {
            // 기존 토큰이 없으면, 새로 저장합니다. INSERT 쿼리가 실행됩니다.
            RefreshTokenEntity savedEntity = repository.save(entityToSave);
            return savedEntity.toDomain();
        }
    }

    @Override
    public Optional<RefreshTokenEntity> findByTokenValue(String tokenValue) {
        return Optional.empty();
    }

    @Override
    public Optional<RefreshTokenEntity> findByMemberId(Long memberId) {
        return Optional.empty();
    }
}
