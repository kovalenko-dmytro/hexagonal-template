package com.gmail.apach.hexagonaltemplate.domain.email.service;

import com.gmail.apach.hexagonaltemplate.application.input.email.GetEmailsInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEmailsService implements GetEmailsInputPort {

    private final GetEmailsOutputPort getEmailsOutputPort;

    @Override
    public Page<Email> getEmails(GetEmailsWrapper wrapper) {
        return getEmailsOutputPort.getEmails(wrapper);
    }
}
