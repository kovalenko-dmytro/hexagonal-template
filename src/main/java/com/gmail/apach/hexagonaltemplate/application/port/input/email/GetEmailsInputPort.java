package com.gmail.apach.hexagonaltemplate.application.port.input.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.email.GetEmailsUseCase;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.wrapper.GetEmailsFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GetEmailsInputPort implements GetEmailsUseCase {

    private final GetEmailsOutputPort getEmailsOutputPort;

    @Override
    public Page<Email> getEmails(
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
        final var filterWrapper = GetEmailsFilterWrapper.builder()
            .sendBy(sendBy).sendTo(sendTo).dateSendFrom(dateSendFrom).dateSendTo(dateSendTo)
            .emailType(emailType).emailStatus(emailStatus).page(page).size(size).sort(sort)
            .build();
        return getEmailsOutputPort.getEmails(filterWrapper);
    }
}