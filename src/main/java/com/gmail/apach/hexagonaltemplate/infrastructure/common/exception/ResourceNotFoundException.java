package com.gmail.apach.hexagonaltemplate.infrastructure.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
