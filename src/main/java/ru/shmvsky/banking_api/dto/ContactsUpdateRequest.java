package ru.shmvsky.banking_api.dto;

public record ContactsUpdateRequest(
        String phone,
        String email
) {
}
