package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email.dto;

import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EmailOutputType(
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
