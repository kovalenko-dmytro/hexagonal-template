package com.gmail.apach.jenkins_demo.application.output.email;

import com.gmail.apach.jenkins_demo.domain.email.model.Email;

public interface GetEmailOutputPort {

    Email getByEmailId(String emailId);
}
