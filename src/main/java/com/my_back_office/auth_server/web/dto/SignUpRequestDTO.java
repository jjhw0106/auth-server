package com.my_back_office.auth_server.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String nickName;
}
