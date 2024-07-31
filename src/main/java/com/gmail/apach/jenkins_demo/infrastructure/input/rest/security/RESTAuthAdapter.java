package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security;

import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInRequest;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.service.AuthService;
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
public class RESTAuthAdapter {

    private final AuthService authService;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok().body(authService.signIn(request));
    }

    @GetMapping(value = "/current-user")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        return ResponseEntity.ok().body(authService.getCurrentUser());
    }
}
