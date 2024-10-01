package com.gmail.apach.hexagonaltemplate.domain.email.wrapper;

import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetEmailsWrapper(
    String sendBy,
    String sendTo,
    LocalDate dateSendFrom,
    LocalDate dateSendTo,
    EmailType emailType,
    EmailStatus emailStatus,
    int page,
    int size,
    String[] sort
) {
}
