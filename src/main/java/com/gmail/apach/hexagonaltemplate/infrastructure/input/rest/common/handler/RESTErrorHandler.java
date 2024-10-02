package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.handler;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class RESTErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        final var errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors()
            .forEach(fieldError -> errors.add(fieldError.getField()
                .concat(CommonConstant.COLON.getValue()).concat(CommonConstant.SPACE.getValue())
                .concat(
                    Objects.nonNull(fieldError.getDefaultMessage())
                        ? fieldError.getDefaultMessage()
                        : CommonConstant.SPACE.getValue())));
        ex.getBindingResult().getGlobalErrors()
            .forEach(objectError -> errors.add(objectError.getObjectName()
                .concat(CommonConstant.COLON.getValue()).concat(CommonConstant.SPACE.getValue())
                .concat(
                    Objects.nonNull(objectError.getDefaultMessage())
                        ? objectError.getDefaultMessage()
                        : CommonConstant.SPACE.getValue())));

        final var params = new Object[]{ex.getBindingResult().getObjectName(), errors.size()};
        final var message =
            messageSource.getMessage(Error.VALIDATION_REQUEST.getKey(), params, LocaleContextHolder.getLocale());
        final var responseDTO = buildRestApiErrorResponse(message, HttpStatus.BAD_REQUEST, errors);
        return createResponseEntity(responseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        final var error = messageSource
            .getMessage(
                Error.MISSING_REQUEST_PARAMETER.getKey(),
                new Object[]{ex.getParameterName()},
                LocaleContextHolder.getLocale());
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, List.of(error));
        return createResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        final var param = String.join(CommonConstant.SPACE.getValue(), ex.getHttpMethod(), ex.getRequestURL());
        final var error =
            messageSource.getMessage(Error.NO_HANDLER_FOUND.getKey(), new Object[]{param}, LocaleContextHolder.getLocale());
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, List.of(error));
        return createResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        String supportedMethods = Objects.nonNull(ex.getSupportedHttpMethods())
            ? ex.getSupportedHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(CommonConstant.SPACE.getValue()))
            : CommonConstant.EMPTY.getValue();
        final var params = new Object[]{ex.getMethod(), supportedMethods};
        String error =
            messageSource.getMessage(Error.HTTP_METHOD_NOT_ALLOWED.getKey(), params, LocaleContextHolder.getLocale());
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, List.of(error));
        return createResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        final var supportedMediaTypes = ex.getSupportedMediaTypes().stream()
            .map(MimeType::toString).collect(Collectors.joining(CommonConstant.SPACE.getValue()));
        final var params = new Object[]{ex.getContentType(), supportedMediaTypes};
        final var error =
            messageSource.getMessage(Error.MEDIA_TYPE_NOT_SUPPORTED.getKey(), params, LocaleContextHolder.getLocale());
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, List.of(error));
        return createResponseEntity(response);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, List.of());
        return createResponseEntity(response);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, List.of());
        return createResponseEntity(response);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleUnauthorized(AuthenticationException ex) {
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, List.of());
        return createResponseEntity(response);
    }

    @ExceptionHandler({ApplicationServerException.class, Exception.class})
    public ResponseEntity<Object> handleGeneralExceptions(Exception ex) {
        final var error = messageSource
            .getMessage(Error.INTERNAL_SERVER_ERROR_OCCURRED.getKey(), null, LocaleContextHolder.getLocale());
        final var response =
            buildRestApiErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, List.of(error));
        return createResponseEntity(response);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorResponse response) {
        return new ResponseEntity<>(response, new HttpHeaders(), response.status());
    }

    private ErrorResponse buildRestApiErrorResponse(
        String exceptionMessage,
        HttpStatus httpStatus,
        List<String> errors) {
        return ErrorResponse.builder()
            .status(httpStatus)
            .message(exceptionMessage)
            .errors(errors)
            .timestamp(LocalDateTime.now().toString())
            .build();
    }
}
