package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email;

import com.gmail.apach.hexagonaltemplate.application.port.input.email.GetEmailsInputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.EmailGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email.dto.EmailOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class GetEmailsGraphQlAdapter {

    private final GetEmailsInputPort getEmailsInputPort;
    private final EmailGraphQlMapper emailGraphQlMapper;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PageOutputType<EmailOutputType> getEmails(
        @Argument(value = "sendBy") String sendBy,
        @Argument(value = "sendTo") String sendTo,
        @Argument(value = "dateSendFrom") LocalDate dateSendFrom,
        @Argument(value = "dateSendTo") LocalDate dateSendTo,
        @Argument(value = "emailType") EmailType emailType,
        @Argument(value = "emailStatus") EmailStatus emailStatus,
        @Argument(value = "page") int page,
        @Argument(value = "size") int size,
        @Argument(value = "sort") List<String> sort
    ) {
        final var emails = getEmailsInputPort.get(
            sendBy, sendTo, dateSendFrom, dateSendTo, emailType, emailStatus, page, size, sort.toArray(String[]::new));
        final var emailsPage = emails.map(emailGraphQlMapper::toEmailOutputType);

        return new PageOutputType<>(emailsPage.getContent(), emailsPage.getSize(), emailsPage.getNumber(),
            emailsPage.getTotalElements(), emailsPage.getTotalPages());
    }
}
