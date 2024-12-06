package com.gmail.apach.hexagonaltemplate.domain.excel.model;

import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ExcelBookTest {

    private static final String FILE_PATH = "src/test/resources/file/insert_users.xlsx";

    @Test
    void buildModel_success() throws IOException {
        final var actual = new ExcelBook(Files.readAllBytes(Paths.get(FILE_PATH)), ExcelFileExtension.XSSF);

        assertNotNull(actual);
        assertNotNull(actual.getSheets());
        assertFalse(actual.getSheets().isEmpty());
        assertEquals(1, actual.getSheets().size());
        assertEquals("USERS", actual.getSheets().getFirst().getTitle());
    }
}