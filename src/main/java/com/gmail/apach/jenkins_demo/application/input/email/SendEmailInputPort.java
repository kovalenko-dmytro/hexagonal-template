package com.gmail.apach.jenkins_demo.application.input.email;

import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;

public interface SendEmailInputPort {

    void sendEmail(SendEmailWrapper sendEmailWrapper);
}
