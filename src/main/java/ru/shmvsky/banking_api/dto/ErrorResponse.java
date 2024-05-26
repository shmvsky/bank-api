package ru.shmvsky.banking_api.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class ErrorResponse {
    private final String message;
    private final String error;
    private final Integer code;
    private final Date timestamp;

    public ErrorResponse(String message, String error, Integer code, Date timestamp) {
        this.message = message;
        this.error = error;
        this.code = code;
        this.timestamp = timestamp;
    }

}
