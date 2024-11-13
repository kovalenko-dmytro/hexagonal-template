package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.springframework.lang.NonNull;

import java.util.Locale;

public class VoidScalar implements Coercing<Object, Object> {

    @Override
    public Object serialize(
        @NonNull Object dataFetcherResult,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingSerializeException {
        return null;
    }

    @Override
    public Object parseValue(
        @NonNull Object input,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingParseValueException {
        return null;
    }

    @Override
    public Object parseLiteral(
        @NonNull Value<?> input,
        @NonNull CoercedVariables variables,
        @NonNull GraphQLContext graphQLContext,
        @NonNull Locale locale
    ) throws CoercingParseLiteralException {
        return null;
    }
}
