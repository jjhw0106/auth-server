package com.my_back_office.auth_server.application.port.in;

public interface LoginUseCaseITF {
    LoginResult login(LoginCommand command);

    record LoginCommand(String email, String password) {}

    record LoginResult(String accessToken, String refreshToken) {}
}
