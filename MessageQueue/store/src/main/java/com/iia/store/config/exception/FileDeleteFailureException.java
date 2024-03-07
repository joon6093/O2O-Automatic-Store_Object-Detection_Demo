package com.iia.store.config.exception;

public class FileDeleteFailureException extends RuntimeException {
    public FileDeleteFailureException() {
    }

    public FileDeleteFailureException(Throwable cause) {
        super(cause);
    }
}