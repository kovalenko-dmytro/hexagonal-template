package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.input.user.GetUsersInputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.wrapper.GetUsersRequestWrapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Users REST API")
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Validated
public class GetUsersRESTAdapter {

    private final GetUsersInputPort getUsersInputPort;
    private final UserRESTMapper userRESTMapper;

    @GetMapping
    public ResponseEntity<PagedModel<UserResponse>> getUsers(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Boolean enabled,
        @RequestParam(required = false) LocalDate createdFrom,
        @RequestParam(required = false) LocalDate createdTo,
        @RequestParam(required = false) String createdBy,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "created") String[] sort
    ) {
        final var wrapper = GetUsersRequestWrapper.builder()
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .enabled(enabled)
            .createdFrom(createdFrom)
            .createdTo(createdTo)
            .createdBy(createdBy)
            .page(page)
            .size(size)
            .sort(sort)
            .build();
        final var users = getUsersInputPort.getUsers(wrapper);
        final var response = users.map(userRESTMapper::toUserResponse);
        return ResponseEntity.ok().body(new PagedModel<>(response));
    }
}
