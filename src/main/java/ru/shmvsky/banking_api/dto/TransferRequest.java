package ru.shmvsky.banking_api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferRequest (
        String to,
        BigDecimal amount
) {
}
