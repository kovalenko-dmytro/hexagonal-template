package com.gmail.apach.jenkins_demo.infrastructure.input.rest.email;

import com.gmail.apach.jenkins_demo.application.input.email.DeleteEmailInputPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email REST API")
@RestController
@RequestMapping(value = "/api/v1/emails/{emailId}")
@RequiredArgsConstructor
@Validated
public class DeleteEmailRESTAdapter {

    private final DeleteEmailInputPort deleteEmailInputPort;

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteByEmailId(@PathVariable(value = "emailId") String emailId) {
        deleteEmailInputPort.deleteByEmailId(emailId);
        return ResponseEntity.noContent().build();
    }
}
