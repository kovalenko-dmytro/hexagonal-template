package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.service;

import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInRequest;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.SignInResponse;

public interface AuthService {

    SignInResponse signIn(SignInRequest signInRequestDTO);
    CurrentUserResponse getCurrentUser();
}
