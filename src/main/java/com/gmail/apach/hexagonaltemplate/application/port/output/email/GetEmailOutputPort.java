package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;

public interface GetEmailOutputPort {
    Email getByEmailId(String emailId);
}
