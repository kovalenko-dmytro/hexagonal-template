package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class LocalDateScalar implements Coercing<LocalDate, String> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Override
    public String serialize(
        @NonNull Object dataFetcherResult,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDate) {
            return ((LocalDate) dataFetcherResult).format(DATE_FORMATTER);
        }
        throw new CoercingSerializeException("Invalid LocalDate value: " + dataFetcherResult);
    }

    @Override
    public LocalDate parseValue(
        @NonNull Object input,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingParseValueException {
        if (input instanceof String) {
            try {
                return LocalDate.parse((String) input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseValueException("Invalid LocalDate value: " + input);
            }
        }
        throw new CoercingParseValueException("Invalid LocalDate value: " + input);
    }

    @Override
    public LocalDate parseLiteral(
        @NonNull Value<?> input,
        @NonNull CoercedVariables variables,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            try {
                return LocalDate.parse(((StringValue) input).getValue(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException("Invalid LocalDate value: " + input);
            }
        }
        throw new CoercingParseLiteralException("Invalid LocalDate value: " + input);
    }
}
