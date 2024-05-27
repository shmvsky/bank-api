package ru.shmvsky.banking_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
        @NotBlank(message = "Username is mandatory")
        private String to;

        @NotNull(message = "Amount is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Must be greater than 0.0")
        @DecimalMax(value = "999999999.99", message = "Maximum allowed value is 999999999.99")
        private BigDecimal amount;
}
