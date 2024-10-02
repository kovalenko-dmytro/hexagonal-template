package com.gmail.apach.hexagonaltemplate.application.output.email;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface SendEmailPublisher {

    void publishInviteEmail(User user);
}
