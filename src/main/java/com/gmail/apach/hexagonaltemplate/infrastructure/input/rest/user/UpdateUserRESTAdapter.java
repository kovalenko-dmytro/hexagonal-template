package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.UpdateUserUseCase;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UpdateUserRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User REST API")
@RestController
@RequestMapping(value = "/api/v1/users/{userId}")
@RequiredArgsConstructor
@Validated
public class UpdateUserRESTAdapter {

    private final UpdateUserUseCase updateUserUseCase;
    private final UserRESTMapper userRESTMapper;

    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable(value = "userId") String userId,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        final var requestedUser = userRESTMapper.toUser(userId, request);
        final var updatedUser = updateUserUseCase.update(requestedUser);
        final var response = userRESTMapper.toUserResponse(updatedUser);
        return ResponseEntity.ok().body(response);
    }
}
