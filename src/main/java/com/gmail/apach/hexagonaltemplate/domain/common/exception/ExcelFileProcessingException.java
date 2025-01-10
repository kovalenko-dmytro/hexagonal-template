package com.gmail.apach.hexagonaltemplate.domain.common.exception;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;

public class ExcelFileProcessingException extends DomainException {

    public ExcelFileProcessingException(DomainError domainError) {
        super(domainError);
    }

    public ExcelFileProcessingException(DomainError domainError, Object[] args) {
        super(domainError, args);
    }
}
