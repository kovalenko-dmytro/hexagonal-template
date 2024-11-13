package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.handler;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import graphql.GraphQLError;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.Objects;

@ControllerAdvice
public class GraphQlErrorHandler {

    @Autowired
    private MessageSource messageSource;

    @GraphQlExceptionHandler
    public GraphQLError handleRequestValidationException(ValidationException ex) {
        return GraphQLError.newError().errorType(graphql.ErrorType.ValidationError).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
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
        return GraphQLError.newError().errorType(graphql.ErrorType.ValidationError).message(message).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        final var message = messageSource
            .getMessage(
                Error.MISSING_REQUEST_PARAMETER.getKey(),
                new Object[]{ex.getParameterName()},
                LocaleContextHolder.getLocale());
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(message).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleNoHandlerFoundException(NoHandlerFoundException ex) {
        final var params = new Object[]{
            String.join(CommonConstant.SPACE.getValue(), ex.getHttpMethod(), ex.getRequestURL())};
        final var message = messageSource.getMessage(
            Error.NO_HANDLER_FOUND.getKey(), params, LocaleContextHolder.getLocale());
        return GraphQLError.newError().errorType(graphql.ErrorType.OperationNotSupported).message(message).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleNotFound(ResourceNotFoundException ex) {
        return GraphQLError.newError().errorType(ErrorType.NOT_FOUND).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleAccessDenied(AccessDeniedException ex) {
        return GraphQLError.newError().errorType(ErrorType.FORBIDDEN).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleUnauthorized(AuthenticationException ex) {
        return GraphQLError.newError().errorType(ErrorType.UNAUTHORIZED).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleDataAccessExceptions(DataAccessException ex) {
        return GraphQLError.newError().errorType(graphql.ErrorType.DataFetchingException).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleApplicationExceptions(ApplicationServerException ex) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGeneralExceptions(Exception ex) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(ex.getMessage()).build();
    }
}
