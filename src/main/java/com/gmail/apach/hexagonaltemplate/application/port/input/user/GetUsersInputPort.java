package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface GetUsersInputPort {
    Page<User> getUsers(String username,
                        String firstName,
                        String lastName,
                        String email,
                        Boolean enabled,
                        LocalDate createdFrom,
                        LocalDate createdTo,
                        String createdBy,
                        int page,
                        int size,
                        String[] sort);
}
