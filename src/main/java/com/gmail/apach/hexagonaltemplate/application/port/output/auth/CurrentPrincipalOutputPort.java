package com.gmail.apach.hexagonaltemplate.application.port.output.auth;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface CurrentPrincipalOutputPort {
    User getPrincipal();
}
