package com.my_back_office.auth_server.application.service;

import com.my_back_office.auth_server.application.port.in.LoginUseCaseITF;
import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;
import com.my_back_office.auth_server.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCaseITF {

    private final MemberRepository memberRepository;

    @Override
    public LoginResult login(LoginCommand command) {
        Email emailToLogin = new Email(command.email());

        Member member = memberRepository.findMemberByEmail(emailToLogin)
            .orElseThrow(() -> new IllegalArgumentException("없는 계정입니다."));

        member.authenticate(command.password());

        return null;
    }
}
