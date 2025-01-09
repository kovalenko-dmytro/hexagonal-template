package com.gmail.apach.hexagonaltemplate.application.usecase.user.batch.job.processor.impl;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.batch.job.processor.FileContentType;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.batch.job.processor.ImportUsersFromFileProcessorStrategy;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.ImportUsersConversionService;
import com.gmail.apach.hexagonaltemplate.domain.user.service.ImportUsersValidationService;
import lombok.Setter;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Component(FileContentType.EXCEL)
@StepScope
@Setter
public class ImportUsersFromExcelProcessorStrategy implements ImportUsersFromFileProcessorStrategy {

    @Value("#{jobParameters['executedBy']}")
    private String executedBy;

    @Override
    public List<User> process(StoredFile file) {
        final var extension = FileNameUtils.getExtension(Paths.get(file.getFileName()));
        final var extensionType = ExcelFileExtension.from(extension);
        final var excel = new ExcelBook(file.getStoredResource().getPayload(), extensionType);

        ImportUsersValidationService.validateExcelFile(excel);
        final var users = ImportUsersConversionService.excelToModel(excel);
        users.forEach(user -> user.setCreatedBy(executedBy));

        ImportUsersValidationService.validateModels(users);
        return users;
    }
}
