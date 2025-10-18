package com.my_back_office.auth_server.application.service;

import com.my_back_office.auth_server.application.port.in.SignUpUseCaseITF;
import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;
import com.my_back_office.auth_server.infra.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService implements SignUpUseCaseITF {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void signUp(SignUpCommand command) {
        Email emailToSignUp = new Email(command.email());
        if(memberRepository.existsMemberByEmail(emailToSignUp)) {
            throw new IllegalArgumentException("Email already in use");
        }

        Member newMember = Member.localRegister(emailToSignUp, command.password(), command.nickname());
        memberRepository.save(newMember);
    }
}
