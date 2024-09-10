package com.gmail.apach.jenkins_demo.domain.email.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String emailId;
    private String sendBy;
    private String sendTo;
    private List<String> cc;
    private String subject;
    private LocalDateTime sendTime;
    private EmailType emailType;
    private EmailStatus emailStatus;
}
