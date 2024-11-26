package com.gmail.apach.hexagonaltemplate.domain.common.exception;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private DomainError domainError;
    private Object[] args;

    public DomainException(String message) {
        super(message);
    }

    public DomainException(DomainError domainError) {
        this.domainError = domainError;
    }

    public DomainException(DomainError domainError, Object[] args) {
        this(domainError);
        this.args = args;
    }
}
