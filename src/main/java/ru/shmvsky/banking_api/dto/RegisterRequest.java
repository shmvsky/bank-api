package ru.shmvsky.banking_api.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RegisterRequest(
        @NotBlank(message = "Username is mandatory")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 9, message = "Password must be at least 6 characters long")
        String password,

        @NotBlank(message = "Full name is mandatory")
        String fullname,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format, use yyyy-MM-dd")
        String birthDate,

        @NotNull(message = "Amount is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Must be greater than 0.0")
        @DecimalMax(value = "999999999.99", message = "Maximum allowed value is 999999999.99")
        BigDecimal amount,

        @Pattern(regexp = "\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}", message = "Phone number is invalid")
        String phone,

        @Email(message = "Email should be valid")
        String email
) {
}
