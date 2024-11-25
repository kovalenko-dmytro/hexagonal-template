package com.gmail.apach.hexagonaltemplate.application.port.output.mq;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;

public interface PublishEmailOutputPort {
    void publishSend(SendEmailWrapper wrapper);
    void publishSave(Email email);
}
