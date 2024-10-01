package com.gmail.apach.hexagonaltemplate.domain.email.service;

import com.gmail.apach.hexagonaltemplate.application.input.email.DeleteEmailInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.email.DeleteEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.output.email.GetEmailOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEmailService implements DeleteEmailInputPort {

    private final GetEmailOutputPort getEmailOutputPort;
    private final DeleteEmailOutputPort deleteEmailOutputPort;

    @Override
    public void deleteByEmailId(String emailId) {
        final var deletedEmail = getEmailOutputPort.getByEmailId(emailId);
        deleteEmailOutputPort.deleteByEmailId(deletedEmail.getEmailId());
    }
}
