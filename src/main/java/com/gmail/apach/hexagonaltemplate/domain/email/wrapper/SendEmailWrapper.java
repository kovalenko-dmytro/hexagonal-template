package com.gmail.apach.hexagonaltemplate.domain.email.wrapper;

import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Builder
public record SendEmailWrapper(
    @NotBlank
    String sendBy,
    @NotBlank
    @Email
    String sendTo,
    List<@NotBlank @Email String> cc,
    @NotBlank
    String subject,
    @NotNull
    EmailType emailType,
    Map<String, Object> properties,
    MultipartFile[] attachments
) {
}
