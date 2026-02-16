package com.my_back_office.auth_server.application.service;

import com.my_back_office.auth_server.application.port.in.LoginUseCaseITF;
import com.my_back_office.auth_server.domain.member.Member;
import com.my_back_office.auth_server.domain.member.vo.Email;
import com.my_back_office.auth_server.domain.member.vo.Role;
import com.my_back_office.auth_server.domain.refreshtoken.RefreshToken;
import com.my_back_office.auth_server.global.jwt.JwtTokenProvider;
import com.my_back_office.auth_server.infra.member.MemberRepository;
import com.my_back_office.auth_server.infra.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCaseITF {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public LoginResult login(LoginCommand command) {
        Email emailToLogin = new Email(command.email());

        Member member = memberRepository.findMemberByEmail(emailToLogin)
            .orElseThrow(() -> new IllegalArgumentException("없는 계정입니다."));

        member.authenticate(command.password());

        // access token 발급
        String accessToken = jwtTokenProvider.generateAccessToken(emailToLogin.value(), Role.ADMIN);
        // refresh token 발급 및 DB 저장
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(emailToLogin.value(), member.getMemberId());

        refreshTokenRepository.save(refreshToken);

        return new LoginResult(accessToken, refreshToken);
    }
}
