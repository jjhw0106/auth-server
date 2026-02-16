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
    /*
    * 최초 로그인일 경우 refreshToken 저장
    * 이미 refreshToken 발급되어 있으면, memberId로 조회 후 token 업데이트*/
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entityToSave = RefreshTokenEntity.fromDomain(refreshToken);

        Optional<RefreshTokenEntity> existingTokenOpt = repository.findByMemberId(refreshToken.getMemberId());

        if (existingTokenOpt.isPresent()) {
            RefreshTokenEntity existingEntity = existingTokenOpt.get();
            existingEntity.updateTokenValue(entityToSave.getRefreshToken());

            RefreshTokenEntity updatedEntity = repository.save(existingEntity);
            return updatedEntity.toDomain();
        } else {
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
