package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.RowCell;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.SheetRow;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImportUsersConversionService {

    public static List<User> excelToModel(ExcelBook excelBook) {
        final var now = LocalDateTime.now();
        return excelBook.getSheets().getFirst().getRows().stream().skip(1)
            .map(row ->
                User.builder()
                    .username(row.getCells().get(0).getValue())
                    .password(row.getCells().get(1).getValue())
                    .firstName(row.getCells().get(2).getValue())
                    .lastName(row.getCells().get(3).getValue())
                    .email(row.getCells().get(4).getValue())
                    .roles(obtainRoles(row))
                    .enabled(true)
                    .created(now)
                    .build())
            .toList();
    }

    private static Set<Role> obtainRoles(SheetRow row) {
        final var result = new HashSet<Role>();
        final var userRoleCell = row.getCells().get(5);
        if (Objects.nonNull(userRoleCell.getValue()) && userRoleCell.getValue().equals(Boolean.TRUE)) {
            result.add(Role.builder().role(RoleType.USER).build());
        }
        RowCell managerRoleCell = row.getCells().get(6);
        if (Objects.nonNull(managerRoleCell.getValue()) && managerRoleCell.getValue().equals(Boolean.TRUE)) {
            result.add(Role.builder().role(RoleType.MANAGER).build());
        }
        return result;
    }
}
