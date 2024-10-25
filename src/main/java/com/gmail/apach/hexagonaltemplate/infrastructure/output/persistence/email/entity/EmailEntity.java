package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.entity;

import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emails")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailEntity {

    @Id
    @UuidGenerator
    @Column(name = "email_", length = 36, unique = true, nullable = false)
    private String emailId;

    @Column(name = "send_by", nullable = false)
    private String sendBy;

    @Column(name = "send_to", nullable = false)
    private String sendTo;


    @Column(
        name = "send_to_cc",
        columnDefinition = "varchar[]"
    )
    private List<String> cc;

    @Column(name = "subject")
    private String subject;

    @Column(name = "send_time", nullable = false)
    private LocalDateTime sendTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailType emailType;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false)
    private EmailStatus emailStatus;
}
