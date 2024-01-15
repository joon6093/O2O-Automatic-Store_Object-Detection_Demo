package com.IIA.o2o_automatic_store_spring.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    BIND_EXCEPTION("bindException.code", "bindException.msg"),
    IMAGE_UPLOAD_FAILURE_EXCEPTION("imageUploadFailureException.code", "imageUploadFailureException.msg");

    private final String code;
    private final String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}