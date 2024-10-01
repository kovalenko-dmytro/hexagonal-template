package com.gmail.apach.hexagonaltemplate.domain.user.wrapper;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetUsersRequestWrapper(
    String username,
    String firstName,
    String lastName,
    String email,
    Boolean enabled,
    LocalDate createdFrom,
    LocalDate createdTo,
    String createdBy,
    int page,
    int size,
    String[] sort
) {
}
