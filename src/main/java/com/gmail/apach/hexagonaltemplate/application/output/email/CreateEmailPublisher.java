package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.dto.SendEmailWrapper;

public interface CreateEmailPublisher {

    void publishCreateEmail(SendEmailWrapper wrapper, EmailStatus emailStatus);
}
