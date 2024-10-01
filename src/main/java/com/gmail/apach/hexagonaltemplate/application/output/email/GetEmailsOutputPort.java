package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import org.springframework.data.domain.Page;

public interface GetEmailsOutputPort {

    Page<Email> getEmails(GetEmailsWrapper wrapper);
}
