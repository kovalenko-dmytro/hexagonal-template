package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;

public interface PublishEmailOutputPort {
    void publishSendEmail(SendEmailWrapper wrapper);
    void publishSaveEmail(Email email);
}
