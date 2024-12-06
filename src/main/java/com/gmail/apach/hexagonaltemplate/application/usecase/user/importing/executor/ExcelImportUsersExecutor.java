package com.gmail.apach.hexagonaltemplate.application.usecase.user.importing.executor;

import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.service.ImportUsersConversionService;
import com.gmail.apach.hexagonaltemplate.domain.user.service.ImportUsersPolicyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component(FileContentType.EXCEL)
@RequiredArgsConstructor
public class ExcelImportUsersExecutor implements ImportUsersExecutor {

    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;
    private final PublishUserOutputPort publishUserOutputPort;

    @Override
    public void execute(StoredFile file) {
        final var extension = FileNameUtils.getExtension(Paths.get(file.getFileName()));
        final var extensionType = ExcelFileExtension.from(extension);
        final var excel = new ExcelBook(file.getStoredResource().getPayload(), extensionType);

        ImportUsersPolicyService.validateExcelFile(excel);
        final var users = ImportUsersConversionService.excelToModel(excel);

        final var principal = currentPrincipalOutputPort.getPrincipal();
        users.forEach(user -> user.setCreatedBy(principal.getUsername()));

        ImportUsersPolicyService.validateModels(users);
        publishUserOutputPort.publishImportUsers(users);
    }
}
