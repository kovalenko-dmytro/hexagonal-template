package com.gmail.apach.hexagonaltemplate.application.port.input.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface GetEmailsInputPort {
    Page<Email> getEmails(
        String sendBy,
        String sendTo,
        LocalDate dateSendFrom,
        LocalDate dateSendTo,
        EmailType emailType,
        EmailStatus emailStatus,
        int page,
        int size,
        String[] sort
    );
}
