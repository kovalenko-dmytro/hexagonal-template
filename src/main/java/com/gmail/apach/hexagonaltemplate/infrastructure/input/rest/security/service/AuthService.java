package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.service;

import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto.SignInRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto.SignInResponse;

public interface AuthService {

    SignInResponse signIn(SignInRequest signInRequestDTO);
    CurrentUserResponse getCurrentUser();
}
