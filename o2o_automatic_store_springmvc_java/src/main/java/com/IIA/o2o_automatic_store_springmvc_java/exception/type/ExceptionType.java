package com.IIA.o2o_automatic_store_springmvc_java.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    BIND_EXCEPTION("bindException.code", "bindException.msg"),
    SNACK_NOT_FOUND_EXCEPTION("snackNotFoundException.code", "snackNotFoundException.msg"),
    IMAGE_READ_EXCEPTION("imageReadException.code", "imageReadException.msg"),
    NULL_RESPONSE_FROM_API_EXCEPTION("nullResponseFromApiException.code", "nullResponseFromApiException.msg"),
    IMAGE_UPLOAD_FAILURE_EXCEPTION("imageUploadFailureException.code", "imageUploadFailureException.msg");

    private final String code;
    private final String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}