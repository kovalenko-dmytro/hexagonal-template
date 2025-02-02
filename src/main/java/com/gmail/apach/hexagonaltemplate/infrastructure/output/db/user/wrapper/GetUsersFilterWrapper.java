package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.common.wrapper.BaseFilterWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class GetUsersFilterWrapper extends BaseFilterWrapper {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
    private LocalDate createdFrom;
    private LocalDate createdTo;
    private String createdBy;
    private boolean isAdmin;
}
