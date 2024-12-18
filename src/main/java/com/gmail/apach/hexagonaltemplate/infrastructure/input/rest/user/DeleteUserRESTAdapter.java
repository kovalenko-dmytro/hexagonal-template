package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.DeleteUserInputPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User REST API")
@RestController
@RequestMapping(value = "/api/v1/users/{userId}")
@RequiredArgsConstructor
@Validated
public class DeleteUserRESTAdapter {

    private final DeleteUserInputPort deleteUserInputPort;

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByUserId(@PathVariable(value = "userId") String userId) {
        deleteUserInputPort.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
