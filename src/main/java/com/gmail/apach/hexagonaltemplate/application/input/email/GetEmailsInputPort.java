package com.gmail.apach.hexagonaltemplate.application.input.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import org.springframework.data.domain.Page;

public interface GetEmailsInputPort {

    Page<Email> getEmails(GetEmailsWrapper wrapper);
}
