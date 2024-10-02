package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.SendEmailWrapper;

public interface CreateEmailPublisher {

    void publishCreateEmail(SendEmailWrapper wrapper, EmailStatus emailStatus);
}
