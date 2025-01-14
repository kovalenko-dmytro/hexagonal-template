package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.impl;

import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobParameterKey;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.FileContentType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.ImportUsersFromFileProcessorStrategy;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.ImportUsersConverter;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.ImportUsersValidator;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Component(FileContentType.EXCEL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportUsersFromExcelProcessorStrategy implements ImportUsersFromFileProcessorStrategy {

    @Override
    public List<User> process(StoredFile file, JobParameters jobParameters) {
        final var extension = FileNameUtils.getExtension(Paths.get(file.getFileName()));
        final var extensionType = ExcelFileExtension.from(extension);
        final var excel = new ExcelBook(file.getStoredResource().getPayload(), extensionType);

        ImportUsersValidator.validateExcelFile(excel);
        final var users = ImportUsersConverter.excelToModel(excel);
        users.forEach(user -> user.setCreatedBy(jobParameters.getString(JobParameterKey.EXECUTED_BY)));

        ImportUsersValidator.validateModels(users);
        return users;
    }
}
