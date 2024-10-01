package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.SendEmailWrapper;

public interface SendEmailPublisher {

    void publish(SendEmailWrapper wrapper);
}
