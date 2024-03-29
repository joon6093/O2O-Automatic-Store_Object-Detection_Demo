package com.iia.store.config.aop;

import org.aspectj.lang.JoinPoint;
import com.iia.store.config.security.PrincipalHandler;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class AssignMemberIdAspect {
    @Before("@annotation(com.iia.store.config.aop.AssignMemberId)")
    public void assignMemberId(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass())
                        .ifPresent(setMemberId -> invokeMethod(setMemberId, arg, PrincipalHandler.extractId())));
    }

    private Optional<Method> getMethod(Class<?> clazz) {
        try {
            return Optional.of(clazz.getMethod("setMemberId", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Method method, Object obj, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}