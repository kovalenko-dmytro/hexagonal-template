package com.gmail.apach.hexagonaltemplate.infrastructure.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException e) throws IOException {
        final var message = messageSource.getMessage(
            Error.UNAUTHORISED.getKey(), null, LocaleContextHolder.getLocale());
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
