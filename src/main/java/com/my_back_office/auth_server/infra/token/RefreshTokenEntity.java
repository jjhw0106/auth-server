package com.my_back_office.auth_server.infra.token;


import com.my_back_office.auth_server.domain.refreshtoken.RefreshToken;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public void updateTokenValue(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        this.createdAt = new Date();
    }

    // Domain 객체를 Entity로 변환
    public static RefreshTokenEntity fromDomain(RefreshToken token) {
        return new RefreshTokenEntity(
            token.getId(),
            token.getMemberId(),
            token.getRefreshToken(),
            token.getCreatedAt()
        );
    }

    // 도메인 객체로 변환(DB에서 조회 후 Application 레이어로 전달 시 필요)
    public RefreshToken toDomain() {
        return RefreshToken.fromPersistence(
            this.id,
            this.memberId,
            this.refreshToken,
            this.createdAt
        );
    }
}