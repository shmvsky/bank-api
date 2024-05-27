package ru.shmvsky.banking_api.exception;

public class EmailAlreadyTaken extends ApplicationError {
    public EmailAlreadyTaken(String email) {
        super(String.format("Email address %s already taken", email));
    }
}
