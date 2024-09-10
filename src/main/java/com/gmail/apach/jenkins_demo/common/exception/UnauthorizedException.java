package com.gmail.apach.jenkins_demo.common.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
