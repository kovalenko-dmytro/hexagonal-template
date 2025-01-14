package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util;

import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ImportUsersConverterTest {

    private static final String FILE_PATH = "src/test/resources/file/insert_users.xlsx";

    @Test
    void excelToModel_success() throws IOException {
        final var excelModel = new ExcelBook(Files.readAllBytes(Paths.get(FILE_PATH)), ExcelFileExtension.XSSF);
        assertNotNull(excelModel);

        final var users = ImportUsersConverter.excelToModel(excelModel);

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(20, users.size());
    }
}