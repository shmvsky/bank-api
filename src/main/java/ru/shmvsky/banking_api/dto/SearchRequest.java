package ru.shmvsky.banking_api.dto;

import lombok.Builder;

@Builder
public record SearchRequest(
        String birthDate,
        String fullname,
        String phone,
        String email,
        String sort_by,
        String order
) {
}
