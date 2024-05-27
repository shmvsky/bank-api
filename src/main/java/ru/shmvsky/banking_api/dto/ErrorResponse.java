package ru.shmvsky.banking_api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ErrorResponse {
    private final String error;
    private final String message;
    @Builder.Default
    private Date timestamp = new Date();

    public ErrorResponse(String error, String message, Date timestamp) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

}
