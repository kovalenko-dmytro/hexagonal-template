package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.auth.AuthService;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.CurrentUserResponse;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.SignInRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.SignInResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication REST API")
@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthUserAdapter {

    private final AuthService authService;
    private final UserRESTMapper userRESTMapper;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest request) {
        final var authTokenDetails = authService.signIn(request.username(), request.password());
        final var response = userRESTMapper.toSignInResponse(authTokenDetails);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/current-user")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        final var currentUser = authService.getCurrentUser();
        final var response = userRESTMapper.toCurrentUserResponse(currentUser);
        return ResponseEntity.ok().body(response);
    }
}
