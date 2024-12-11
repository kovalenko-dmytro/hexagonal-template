package com.gmail.apach.hexagonaltemplate.application.port.input.user;

public interface ImportUsersInputPort {
    void execute(String batchId, String fileId, String username);
}
