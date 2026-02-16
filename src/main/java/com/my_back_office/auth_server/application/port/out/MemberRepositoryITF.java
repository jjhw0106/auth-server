package com.my_back_office.auth_server.application.port.out;

import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;

import java.util.Optional;

public interface MemberRepositoryITF {
    Member save(Member member);

    Optional<Member> findMemberById(String id);

    Optional<Member> findMemberByEmail(Email email);

    boolean existsMemberById(String id);

    boolean existsMemberByEmail(Email email);
}
