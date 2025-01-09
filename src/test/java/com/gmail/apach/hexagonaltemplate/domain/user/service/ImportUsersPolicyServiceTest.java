package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import com.gmail.apach.hexagonaltemplate.domain.user.constant.ImportUsersExcelHeaders;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ImportUsersPolicyServiceTest {

    private static final String FILE_PATH = "src/test/resources/file/insert_users.xlsx";
    private static final String FILE_PATH_WRONG_HEADERS = "src/test/resources/file/insert_users_wrong_headers.xlsx";
    private static final String FILE_PATH_WRONG_STRING = "src/test/resources/file/insert_users_wrong_string.xlsx";
    private static final String FILE_PATH_WRONG_BOOLEAN = "src/test/resources/file/insert_users_wrong_boolean.xlsx";
    private static final String FILE_PATH_WRONG_EMPTY = "src/test/resources/file/insert_users_wrong_empty.xlsx";

    @Test
    void validateExcelFile_success() throws IOException {
        final var excelBook = new ExcelBook(Files.readAllBytes(Paths.get(FILE_PATH)), ExcelFileExtension.XSSF);
        assertNotNull(excelBook);

        assertDoesNotThrow(() -> ImportUsersPolicyService.validateExcelFile(excelBook));
    }

    @Test
    void validateExcelFile_sheetNotExist_fail() {
        final var excelBook = mock(ExcelBook.class);
        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateExcelFile(excelBook),
            "Sheet with name USERS does not exist");
    }

    @Test
    void validateExcelFile_sheetHeaders_fail() throws IOException {
        final var excelBook = new ExcelBook(
            Files.readAllBytes(Paths.get(FILE_PATH_WRONG_HEADERS)),
            ExcelFileExtension.XSSF);

        assertNotNull(excelBook);

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateExcelFile(excelBook),
            """
                Sheet headers must be %s and must not contain duplicates
                """.formatted(ImportUsersExcelHeaders.headersJoinToString())
        );
    }

    @Test
    void validateExcelFile_wrongString_fail() throws IOException {
        final var excelBook = new ExcelBook(
            Files.readAllBytes(Paths.get(FILE_PATH_WRONG_STRING)),
            ExcelFileExtension.XSSF);

        assertNotNull(excelBook);

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateExcelFile(excelBook),
            """
                Values of %s must have string data type
                """.formatted(ImportUsersExcelHeaders.stringTypeheadersJoinToString())
        );
    }

    @Test
    void validateExcelFile_wrongBoolean_fail() throws IOException {
        final var excelBook = new ExcelBook(
            Files.readAllBytes(Paths.get(FILE_PATH_WRONG_BOOLEAN)),
            ExcelFileExtension.XSSF);

        assertNotNull(excelBook);

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateExcelFile(excelBook),
            """
                Values of %s must have boolean data type
                """.formatted(ImportUsersExcelHeaders.booleanTypeheadersJoinToString())
        );
    }

    @Test
    void validateExcelFile_wrongEmpty_fail() throws IOException {
        final var excelBook = new ExcelBook(
            Files.readAllBytes(Paths.get(FILE_PATH_WRONG_EMPTY)),
            ExcelFileExtension.XSSF);

        assertNotNull(excelBook);

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateExcelFile(excelBook),
            "Cell values can not be blank and must be present"
        );
    }

    @Test
    void validateModels_success() {
        final var users = UsersTestData.usersForInsert();
        assertDoesNotThrow(() -> ImportUsersPolicyService.validateModels(users));
    }

    @Test
    void validateModels_nonValidUsername_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user -> user.setUsername(StringUtils.EMPTY));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                Username must have at least %s characters and less than %s characters
                """.formatted(2, 255)
        );
    }

    @Test
    void validateModels_nonValidPassword_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user -> user.setPassword("pas"));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                Password must have at least %s characters and less than %s characters
                """.formatted(2, 255)
        );
    }

    @Test
    void validateModels_nonValidFirstName_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user -> user.setFirstName("n"));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                First name must have at least %s characters and less than %s characters
                """.formatted(2, 50)
        );
    }

    @Test
    void validateModels_nonValidLastName_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user -> user.setLastName(RandomStringUtils.random(51)));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                Last name must have at least %s characters and less than %s characters
                """.formatted(2, 50)
        );
    }

    @Test
    void validateModels_nonValidEmail_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user -> user.setEmail(RandomStringUtils.random(256)));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                Email must have less than %s characters and must be a valid email
                """.formatted(255)
        );
    }

    @Test
    void validateModels_nonValidRole_fail() {
        final var users = UsersTestData.usersForInsert();
        users.forEach(user ->
            user.setRoles(Set.of(Role.builder().role(RoleType.ADMIN).build())));

        assertThrows(
            PolicyViolationException.class,
            () -> ImportUsersPolicyService.validateModels(users),
            """
                Users roles must not contain %s role
                """.formatted(RoleType.ADMIN.name())
        );
    }
}