package com.gmail.apach.hexagonaltemplate.application.input.email;

import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.SendEmailWrapper;

public interface SendEmailInputPort {

    void sendEmail(SendEmailWrapper sendEmailWrapper);
}
