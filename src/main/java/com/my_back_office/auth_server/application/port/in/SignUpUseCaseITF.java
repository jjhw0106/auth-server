package com.my_back_office.auth_server.application.port.in;

public interface SignUpUseCaseITF {
    void signUp(SignUpCommand command);

    record SignUpCommand(
        String email,
        String password,
        String nickname
    ) {}
}
