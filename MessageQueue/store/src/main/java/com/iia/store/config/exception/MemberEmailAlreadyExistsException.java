package com.iia.store.config.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException {
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}