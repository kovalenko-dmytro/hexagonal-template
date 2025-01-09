package com.gmail.apach.hexagonaltemplate.application.port.input.user;

public interface ImportUsersFromFileInputPort {
    void execute(String batchId, String fileId, String executedBy);
}
