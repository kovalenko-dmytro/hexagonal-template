package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email.helper;

import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailType;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.dto.SendEmailWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrepareEmailPayloadHelper {

    public static SendEmailWrapper prepareEmailData(User user) {
        return SendEmailWrapper.builder()
            .sendBy(user.getCreatedBy())
            .sendTo(user.getEmail())
            .properties(Map.of(
                EmailType.Property.RECIPIENT_NAME.getProperty(), user.getUsername(),
                EmailType.Property.SENDER_NAME.getProperty(), user.getCreatedBy()
            ))
            .subject("Hello")
            .emailType(EmailType.INVITE)
            .build();
    }
}
