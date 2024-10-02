package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User REST API")
@RestController
@RequestMapping(value = "/api/v1/users/{userId}")
@RequiredArgsConstructor
@Validated
public class GetUserRESTAdapter {

    private final GetUserInputPort getUserInputPort;
    private final UserRESTMapper userRESTMapper;

    @GetMapping
    public ResponseEntity<UserResponse> getByUserId(@PathVariable(value = "userId") String userId) {
        final var requestedUser = getUserInputPort.getByUserId(userId);
        final var response = userRESTMapper.toUserResponse(requestedUser);
        return ResponseEntity.ok().body(response);
    }
}
