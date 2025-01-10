package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.common.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JdbcColumnConverter {

    public static LocalDateTime toLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return Objects.nonNull(timestamp) ? timestamp.toLocalDateTime() : null;
    }
}
