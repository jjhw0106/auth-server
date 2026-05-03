package com.my_back_office.auth_server.web;

import com.my_back_office.auth_server.application.port.in.LoginUseCaseITF;
import com.my_back_office.auth_server.application.port.in.SignUpUseCaseITF;
import com.my_back_office.auth_server.application.service.LoginService;
import com.my_back_office.auth_server.application.service.SignUpService;
import com.my_back_office.auth_server.web.dto.LoginRequestDTO;
import com.my_back_office.auth_server.web.dto.LoginRequestDTO;
import com.my_back_office.auth_server.web.dto.SignUpRequestDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.my_back_office.auth_server.web.dto.LoginResponseDTO;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginService loginService;
    private final SignUpService signUpService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        System.out.println("login!!");
        LoginUseCaseITF.LoginCommand command = new LoginUseCaseITF.LoginCommand(request.getEmail(), request.getPassword());

        LoginUseCaseITF.LoginResult result = loginService.login(command);

        // access token을 HttpOnly 쿠키로 전달
        Cookie cookie = new Cookie("accessToken", result.accessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        // 응답 본문: 사용자 정보 객체
        LoginResponseDTO responseDTO = new LoginResponseDTO(request.getEmail(), result.nickname(), result.role());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequestDTO request) {
        System.out.println("signup!!");
        try {
            SignUpUseCaseITF.SignUpCommand command = new SignUpUseCaseITF.SignUpCommand(
                request.getEmail(),
                request.getPassword(),
                request.getNickName()
            );

            signUpService.signUp(command);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            // 이메일 중복 등의 유효성 검증 실패
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
        }
    }
}
