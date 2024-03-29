package com.IIA.o2o_automatic_store_springmvc_java.exception.handler;

import com.IIA.o2o_automatic_store_springmvc_java.dto.response.Response;
import com.IIA.o2o_automatic_store_springmvc_java.exception.type.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final MessageSource messageSource;

    public Response getFailureResponse(ExceptionType exceptionType) {
        return Response.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage()));
    }

    public Response getFailureResponse(ExceptionType exceptionType, Object... args) {
        return Response.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage(), args));
    }

    private Integer getCode(String key) {
        return Integer.valueOf(messageSource.getMessage(key, null, LocaleContextHolder.getLocale()));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key,null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}