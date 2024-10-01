package com.gmail.apach.hexagonaltemplate.data;

import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.entity.EmailEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailsTestData {

    public static Email email() {
        return Email.builder()
            .sendBy("admin")
            .sendTo("email@email.com")
            .cc(List.of("cc@gmail.com", "cc1@gmail.com"))
            .subject("subject")
            .sendTime(LocalDateTime.now())
            .emailType(EmailType.INVITE)
            .emailStatus(EmailStatus.SEND)
            .build();
    }

    public static EmailEntity emailEntity() {
        return EmailEntity.builder()
            .sendBy("admin")
            .sendTo("email@email.com")
            .cc(List.of("cc@gmail.com", "cc1@gmail.com"))
            .subject("subject")
            .sendTime(LocalDateTime.now())
            .emailType(EmailType.INVITE)
            .emailStatus(EmailStatus.SEND)
            .build();
    }

    public static Email anotherEmail() {
        return Email.builder()
            .sendBy("admin")
            .sendTo("anotherEmail@email.com")
            .cc(List.of("cc@gmail.com", "cc1@gmail.com"))
            .subject("anotherSubject")
            .sendTime(LocalDateTime.now())
            .emailType(EmailType.INVITE)
            .emailStatus(EmailStatus.ERROR)
            .build();
    }

    public static List<EmailEntity> emails() {
        return List.of(
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email1@email.com")
                .cc(List.of("cc@gmail.com", "cc1@gmail.com"))
                .subject("subject1")
                .sendTime(LocalDateTime.of(2024, 8, 24, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.SEND)
                .build(),
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email2@email.com")
                .cc(List.of("cc2@gmail.com"))
                .subject("subject2")
                .sendTime(LocalDateTime.of(2024, 8, 24, 14, 14, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.SEND)
                .build(),
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email3@email.com")
                .subject("subject3")
                .sendTime(LocalDateTime.of(2024, 8, 25, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.SEND)
                .build(),
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email4@email.com")
                .subject("subject4")
                .sendTime(LocalDateTime.of(2024, 8, 26, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.SEND)
                .build(),
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email5@email.com")
                .subject("subject5")
                .sendTime(LocalDateTime.of(2024, 8, 27, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.SEND)
                .build(),
            EmailEntity.builder()
                .sendBy("admin")
                .sendTo("email1@email.com")
                .subject("subject6")
                .sendTime(LocalDateTime.of(2024, 8, 28, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.ERROR)
                .build(),
            EmailEntity.builder()
                .sendBy("manager")
                .sendTo("email7@email.com")
                .subject("subject7")
                .sendTime(LocalDateTime.of(2024, 8, 29, 11, 11, 11))
                .emailType(EmailType.INVITE)
                .emailStatus(EmailStatus.ERROR)
                .build()
        );
    }
}
