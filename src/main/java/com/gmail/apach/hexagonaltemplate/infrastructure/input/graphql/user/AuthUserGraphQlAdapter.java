package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.auth.AuthService;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.UserGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.CurrentUserOutputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.SignInInputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.SignInOutputType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class AuthUserGraphQlAdapter {

    private final AuthService authService;
    private final UserGraphQlMapper userGraphQlMapper;

    @QueryMapping
    public SignInOutputType signIn(@Valid @Argument(value = "inputType") SignInInputType inputType) {
        final var authTokenDetails = authService.signIn(inputType.username(), inputType.password());
        return userGraphQlMapper.toSignInOutputType(authTokenDetails);
    }

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public CurrentUserOutputType getCurrentUser() {
        final var currentUser = authService.getCurrentUser();
        return userGraphQlMapper.toCurrentUserOutputType(currentUser);
    }
}
