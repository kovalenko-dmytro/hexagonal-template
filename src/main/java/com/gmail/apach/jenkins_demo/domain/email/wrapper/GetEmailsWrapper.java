package com.gmail.apach.jenkins_demo.domain.email.wrapper;

import com.gmail.apach.jenkins_demo.domain.email.model.EmailStatus;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
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
