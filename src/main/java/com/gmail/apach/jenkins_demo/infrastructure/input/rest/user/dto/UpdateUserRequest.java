package com.gmail.apach.jenkins_demo.infrastructure.input.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequest(
    @Size(min = 2, max = 50)
    String firstName,
    @Size(min = 2, max = 50)
    String lastName
) {

}
