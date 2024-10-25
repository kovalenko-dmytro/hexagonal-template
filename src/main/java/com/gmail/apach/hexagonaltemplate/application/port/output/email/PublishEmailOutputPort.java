package com.gmail.apach.hexagonaltemplate.application.port.output.email;

import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.dto.SendEmailWrapper;

public interface PublishEmailOutputPort {
    void publishSendInviteEmail(User user);
    void publishCreateEmail(SendEmailWrapper wrapper, EmailStatus emailStatus);
}
