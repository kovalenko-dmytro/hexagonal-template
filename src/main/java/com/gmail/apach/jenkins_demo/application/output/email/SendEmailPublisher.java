package com.gmail.apach.jenkins_demo.application.output.email;

import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;

public interface SendEmailPublisher {

    void publish(SendEmailWrapper wrapper);
}
