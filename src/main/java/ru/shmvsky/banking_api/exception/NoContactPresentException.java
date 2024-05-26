package ru.shmvsky.banking_api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(UNPROCESSABLE_ENTITY)
public class NoContactPresentException extends ApplicationError {
    public NoContactPresentException() {
        super("User must have email or phone number.");
    }
}
