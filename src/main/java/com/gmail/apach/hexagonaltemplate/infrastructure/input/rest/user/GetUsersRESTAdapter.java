package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.GetUsersUseCase;
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

@Tag(name = "User REST API")
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Validated
public class GetUsersRESTAdapter {

    private final GetUsersUseCase getUsersUseCase;
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
        final var users = getUsersUseCase.getUsers(username, firstName, lastName, email, enabled,
            createdFrom, createdTo, createdBy, page, size, sort);
        final var response = users.map(userRESTMapper::toUserResponse);
        return ResponseEntity.ok().body(new PagedModel<>(response));
    }
}
