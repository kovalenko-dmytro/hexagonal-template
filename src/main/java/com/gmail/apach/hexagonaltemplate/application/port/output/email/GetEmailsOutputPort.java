package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.wrapper.GetEmailsFilterWrapper;
import org.springframework.data.domain.Page;

public interface GetEmailsOutputPort {
    Page<Email> getEmails(GetEmailsFilterWrapper wrapper);
}
