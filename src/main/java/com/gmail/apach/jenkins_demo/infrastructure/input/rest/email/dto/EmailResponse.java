package com.gmail.apach.jenkins_demo.infrastructure.input.rest.email.dto;

import com.gmail.apach.jenkins_demo.domain.email.model.EmailStatus;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EmailResponse(
    String emailId,
    String sendBy,
    String sendTo,
    List<String> cc,
    String subject,
    LocalDateTime sendTime,
    EmailType emailType,
    EmailStatus emailStatus
) {
}
