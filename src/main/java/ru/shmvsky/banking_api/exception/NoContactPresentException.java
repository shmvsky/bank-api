package ru.shmvsky.banking_api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class NoContactPresentException extends ApplicationError {
    public NoContactPresentException() {
        super("User must have email or phone number.");
    }
}
