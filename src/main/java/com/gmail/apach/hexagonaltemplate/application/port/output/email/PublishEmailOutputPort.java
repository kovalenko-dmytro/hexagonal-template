package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface PublishEmailOutputPort {
    void publishSendInviteEmail(User user);
    void publishSaveEmail(Email email);
}
