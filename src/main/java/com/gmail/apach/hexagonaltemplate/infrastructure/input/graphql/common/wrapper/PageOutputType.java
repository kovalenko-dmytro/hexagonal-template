package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper;

import java.util.List;

public record PageOutputType<T>(
    List<T> content,
    int size,
    int number,
    long totalElements,
    int totalPages
) {
    public PageOutputType(List<T> content, int size, int number, long totalElements, int totalPages) {
        this.content = content;
        this.size = size;
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
