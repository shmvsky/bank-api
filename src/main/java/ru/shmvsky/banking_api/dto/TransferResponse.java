package ru.shmvsky.banking_api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferResponse (
        String to,
        BigDecimal amount,
        BigDecimal balance,
        String status
) {
}
