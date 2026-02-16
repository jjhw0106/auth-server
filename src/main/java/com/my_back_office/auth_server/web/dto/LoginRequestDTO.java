package com.my_back_office.auth_server.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}