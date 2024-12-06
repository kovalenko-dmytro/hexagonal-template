package com.gmail.apach.hexagonaltemplate.application.port.output.user;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.ImportUsersWrapper;

public interface ImportUsersOutputPort {
    void execute(ImportUsersWrapper wrapper);
}
