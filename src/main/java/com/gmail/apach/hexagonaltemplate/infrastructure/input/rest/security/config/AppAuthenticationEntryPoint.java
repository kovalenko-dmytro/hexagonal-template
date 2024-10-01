package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.apach.hexagonaltemplate.common.constant.message.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException e) throws IOException {
        final var message = messageSource.getMessage(Error.UNAUTHORISED.getKey(), null, Locale.ENGLISH);
        final var responseDTO =
            ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(message)
                .errors(List.of(e.getMessage()))
                .timestamp(LocalDateTime.now().toString())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final var mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(responseDTO));
    }
}
