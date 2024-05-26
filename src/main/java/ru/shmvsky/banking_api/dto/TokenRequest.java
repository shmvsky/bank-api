package ru.shmvsky.banking_api.dto;

import lombok.Builder;


@Builder
public record TokenRequest(
        String username,
        String password
) {

}
