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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        // access token 발급
        String accessToken = loginService.login(command).accessToken();
        Cookie cookie = new Cookie("accessToken", accessToken);

        // httpOnly 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        // 응답 본문 전달(사용자 정보, 메시지)
        return new ResponseEntity<>(request.getEmail(), HttpStatus.OK);
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

    /*게이트웨이 실행 안시켰을 때, CORS 등록*/
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173")); // 프론트 포트
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true); // 필요에 따라 false로 변경 가능
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
