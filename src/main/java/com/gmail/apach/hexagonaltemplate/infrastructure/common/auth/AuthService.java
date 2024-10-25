package com.gmail.apach.hexagonaltemplate.infrastructure.common.auth;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.GetUserUseCase;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.AuthTokenDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GetUserUseCase getUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthTokenDetails signIn(String username, String password) {
        final var authToken = new UsernamePasswordAuthenticationToken(username, password);
        final var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var authenticatedUser = getUserUseCase.getByUsername(authentication.getName());
        final var accessToken = jwtService.generateAccessToken(authenticatedUser);
        return AuthTokenDetails.builder()
            .tokenType(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue().trim())
            .accessToken(accessToken)
            .accessTokenExpired(jwtService.getAccessTokenExpirationTime())
            .build();
    }

    public User getCurrentUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserUseCase.getByUsername(authentication.getName());
    }
}
