package ru.shmvsky.banking_api.exception;

public class ApplicationError extends RuntimeException {
    public ApplicationError(String message) {
        super(message);
    }
}
