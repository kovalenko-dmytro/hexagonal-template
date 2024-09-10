package com.gmail.apach.jenkins_demo.application.output.email;

import com.gmail.apach.jenkins_demo.domain.email.model.Email;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.GetEmailsWrapper;
import org.springframework.data.domain.Page;

public interface GetEmailsOutputPort {

    Page<Email> getEmails(GetEmailsWrapper wrapper);
}
