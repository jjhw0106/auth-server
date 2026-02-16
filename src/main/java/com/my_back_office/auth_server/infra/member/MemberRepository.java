package com.my_back_office.auth_server.infra.member;
import com.my_back_office.auth_server.application.port.out.MemberRepositoryITF;
import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;
import com.my_back_office.auth_server.infra.jpa.MemberJpaRepository;
import com.my_back_office.auth_server.infra.jpa.RefreshJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements MemberRepositoryITF {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        MemberEntity entity = MemberEntity.fromDomain(member);

        MemberEntity savedEntity = memberJpaRepository.save(entity);

        return savedEntity.toDomain();
    }

    @Override
    public Optional<Member> findMemberById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findMemberByEmail(Email email) {
        String input = email.value();
        Optional<MemberEntity> selectedMember = memberJpaRepository.findByEmail(input);
        System.out.println("findMemberByEmail!!");
        return selectedMember.map(MemberEntity::toDomain);
    }

    @Override
    public boolean existsMemberById(String id) {
        return false;
    }

    @Override
    public boolean existsMemberByEmail(Email email) {
        String emailValue = email.value();
        return memberJpaRepository.existsByEmail(emailValue);
    }
}
