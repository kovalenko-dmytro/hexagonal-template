package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email;

import com.gmail.apach.hexagonaltemplate.application.port.input.email.DeleteEmailInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class DeleteEmailGraphQlAdapter {

    private final DeleteEmailInputPort deleteEmailInputPort;

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteByEmailId(@Argument(value = "emailId") String emailId) {
        deleteEmailInputPort.deleteByEmailId(emailId);
    }
}
