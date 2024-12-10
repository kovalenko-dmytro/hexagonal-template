package com.gmail.apach.hexagonaltemplate.application.port.input.user;

public interface ImportUsersInputPort {
    void execute(String jobId, String fileId, String username);
}
