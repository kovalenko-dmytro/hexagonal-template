package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.wrapper;

import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.common.BaseFilterWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class GetEmailsFilterWrapper extends BaseFilterWrapper {

    private String sendBy;
    private String sendTo;
    private LocalDate dateSendFrom;
    private LocalDate dateSendTo;
    private EmailType emailType;
    private EmailStatus emailStatus;
}
