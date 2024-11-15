package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;

public interface SendEmailOutputPort {
    void sendEmail(SendEmailWrapper sendEmailWrapper);
}
