package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.user.constant.ImportUsersExcelHeaders;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.importing.*;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImportUsersPolicyService {

    public static void validateExcelFile(ExcelBook excelBook) {
        final var policy =
            new ExcelSheetExistsPolicy()
                .withErrorDetails(
                    DomainError.ERROR_EXCEL_SHEET_NOT_EXIST,
                    new Object[]{ExcelSheetExistsPolicy.SHEET_TITLE})
                .terminateIfError()
                .and(new ExcelHeadersExistPolicy()
                    .withErrorDetails(
                        DomainError.ERROR_EXCEL_SHEET_HEADERS,
                        new Object[]{ImportUsersExcelHeaders.headersJoinToString()})
                    .terminateIfError())
                .and(new ExcelCellDataTypesIsStringPolicy()
                    .withErrorDetails(
                        DomainError.ERROR_EXCEL_DATA_TYPE_MUST_STRING,
                        new Object[]{ImportUsersExcelHeaders.stringTypeheadersJoinToString()})
                    .terminateIfError())
                .and(new ExcelCellDataTypesIsBooleanPolicy()
                    .withErrorDetails(
                        DomainError.ERROR_EXCEL_DATA_TYPE_MUST_BOOLEAN,
                        new Object[]{ImportUsersExcelHeaders.booleanTypeheadersJoinToString()})
                    .terminateIfError())
                .and(new ExcelCellValuesExistPolicy()
                    .withErrorDetails(DomainError.ERROR_EXCEL_VALUES_MUST_PRESENT)
                    .terminateIfError());

        policy.isSatisfiedWith(excelBook);
    }

    public static void validateModels(List<User> users) {
        users.forEach(user -> {
            final var policy = new UsernamePolicy()
                .withErrorDetails(DomainError.ERROR_USER_USERNAME, new Object[]{2, 255})
                .terminateIfError()
                .and(new PasswordPolicy()
                    .withErrorDetails(DomainError.ERROR_USER_PASSWORD, new Object[]{4, 255})
                    .terminateIfError())
                .and(new FirstNamePolicy()
                    .withErrorDetails(DomainError.ERROR_USER_FIRST_NAME, new Object[]{2, 50})
                    .terminateIfError())
                .and(new LastNamePolicy()
                    .withErrorDetails(DomainError.ERROR_USER_LAST_NAME, new Object[]{2, 50})
                    .terminateIfError())
                .and(new EmailPolicy()
                    .withErrorDetails(DomainError.ERROR_USER_EMAIL, new Object[]{255})
                    .terminateIfError())
                .and(new RoleTypePolicy()
                    .withErrorDetails(DomainError.ERROR_USER_ROLE, new Object[]{RoleType.ADMIN})
                    .terminateIfError());

            policy.isSatisfiedWith(user);
        });
    }
}
