package com.my_back_office.auth_server.infra.member;

import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;
import com.my_back_office.auth_server.domain.member.vo.HashedPassword;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String provider;

    private String providerId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static MemberEntity fromDomain(Member member) {
        return new MemberEntity(
            member.getMemberId(),
            member.getEmail().value(),
            member.getPassword() != null ? member.getPassword().value() : null,
            member.getNickname(),
            member.getRole().name(),
            member.getStatus().name(),
            member.getProvider().name(),
            member.getProviderId(),
            member.getCreatedAt(),
            member.getUpdatedAt()
        );
    }

    /**
     * 엔티티를 도메인 객체로 변환하는 메서드
     */
    public Member toDomain() {
        return Member.fromPersistence(
            this.memberId,
            new Email(this.email),
            this.password != null ? new HashedPassword(this.password) : null,
            this.nickname,
            this.role,
            this.status,
            this.provider,
            this.providerId,
            this.createdAt,
            this.updatedAt
        );
    }
}