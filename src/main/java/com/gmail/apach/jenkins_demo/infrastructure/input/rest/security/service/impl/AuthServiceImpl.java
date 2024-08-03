package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.service.impl;

import com.gmail.apach.jenkins_demo.application.input.user.GetUserInputPort;
import com.gmail.apach.jenkins_demo.common.constant.CommonConstant;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInRequest;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.service.AuthService;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final GetUserInputPort getUserInputPort;

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRESTMapper userMapper;

    @Override
    public SignInResponse signIn(SignInRequest request) {
        final var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        final var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var authenticatedUser = getUserInputPort.getByUsername(authentication.getName());
        final var accessToken = jwtService.generateAccessToken(authenticatedUser);
        return SignInResponse.builder()
            .tokenType(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue().trim())
            .accessToken(accessToken)
            .accessTokenExpired(jwtService.getAccessTokenExpirationTime())
            .build();
    }

    @Override
    public CurrentUserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final var currentUser = getUserInputPort.getByUsername(authentication.getName());
        return userMapper.toCurrentUserResponse(currentUser);
    }
}