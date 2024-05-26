package ru.shmvsky.banking_api.dto;

import lombok.Builder;


@Builder
public record TokenResponse(
        String token
) {
}
