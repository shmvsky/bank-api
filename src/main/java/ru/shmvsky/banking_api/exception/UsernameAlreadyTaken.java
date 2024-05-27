package ru.shmvsky.banking_api.exception;

public class UsernameAlreadyTaken extends ApplicationError {
    public UsernameAlreadyTaken(String username) {
        super(String.format("Username %s already taken", username));
    }
}
