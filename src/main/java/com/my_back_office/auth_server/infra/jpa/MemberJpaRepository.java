package com.my_back_office.auth_server.infra.jpa;

import com.my_back_office.auth_server.infra.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByEmail(String email);

    Optional<MemberEntity> findByEmail(String email);


}
