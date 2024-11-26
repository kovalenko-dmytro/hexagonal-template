package com.gmail.apach.hexagonaltemplate.domain.common.exception;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;

public class PolicyViolationException extends DomainException {

    public PolicyViolationException(DomainError domainError) {
        super(domainError);
    }

    public PolicyViolationException(DomainError domainError, Object[] args) {
        super(domainError, args);
    }
}
