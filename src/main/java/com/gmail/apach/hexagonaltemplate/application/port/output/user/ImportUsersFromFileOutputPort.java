package com.gmail.apach.hexagonaltemplate.application.port.output.user;

public interface ImportUsersFromFileOutputPort {
    void execute(String batchId, String fileId, String executedBy);
}
