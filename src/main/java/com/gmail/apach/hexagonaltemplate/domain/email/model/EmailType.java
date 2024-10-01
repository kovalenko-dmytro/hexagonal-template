package com.gmail.apach.hexagonaltemplate.domain.email.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailType {

    INVITE("invite-letter.html");

    private final String template;

    @RequiredArgsConstructor
    @Getter
    public enum Property {

        RECIPIENT_NAME("recipientName"),
        SENDER_NAME("senderName");

        private final String property;
    }
}
