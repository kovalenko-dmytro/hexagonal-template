package com.gmail.apach.jenkins_demo.infrastructure.input.rest.email;

import com.gmail.apach.jenkins_demo.application.input.email.GetEmailsInputPort;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailStatus;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.GetEmailsWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.mapper.EmailRESTMapper;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.email.dto.EmailResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Email REST API")
@RestController
@RequestMapping(value = "/api/v1/emails")
@RequiredArgsConstructor
@Validated
public class GetEmailsRESTAdapter {

    private final GetEmailsInputPort getEmailsInputPort;
    private final EmailRESTMapper emailRESTMapper;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PagedModel<EmailResponse>> getEmails(
        @RequestParam(required = false) String sendBy,
        @RequestParam(required = false) String sendTo,
        @RequestParam(required = false) LocalDate dateSendFrom,
        @RequestParam(required = false) LocalDate dateSendTo,
        @RequestParam(required = false) EmailType emailType,
        @RequestParam(required = false) EmailStatus emailStatus,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "sendBy") String[] sort
    ) {
        final var wrapper = GetEmailsWrapper.builder()
            .sendBy(sendBy)
            .sendTo(sendTo)
            .dateSendFrom(dateSendFrom)
            .dateSendTo(dateSendTo)
            .emailType(emailType)
            .emailStatus(emailStatus)
            .page(page)
            .size(size)
            .sort(sort)
            .build();

        final var emails = getEmailsInputPort.getEmails(wrapper);
        final var response = emails.map(emailRESTMapper::toEmailResponse);
        return ResponseEntity.ok().body(new PagedModel<>(response));
    }
}