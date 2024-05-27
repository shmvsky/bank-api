package ru.shmvsky.banking_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
public class TokenRequest {
        @NotBlank(message = "Username is mandatory")
        private String username;

        @NotBlank(message = "Password must not be blank")
        private String password;
}
