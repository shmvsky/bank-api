package ru.shmvsky.banking_api.exception;

public class PhoneNumberAlreadyTaken extends ApplicationError {
    public PhoneNumberAlreadyTaken(String pnone) {
        super(String.format("Phone number %s already taken", pnone));
    }
}
