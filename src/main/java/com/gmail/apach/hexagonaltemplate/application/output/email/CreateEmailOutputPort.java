package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;

public interface CreateEmailOutputPort {

    Email createEmail(Email email);
}
