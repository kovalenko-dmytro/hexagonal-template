package com.gmail.apach.hexagonaltemplate.domain.excel.vo;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.ExcelFileProcessingException;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.cell.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class RowCell implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private Integer index;
    private CellDataType dataType;

    public abstract <T> T getValue();

    public static RowCell getInstance(Cell cell) {
        final var dataType = cell.getCellType();
        final int columnIndex = cell.getColumnIndex();
        switch (dataType) {
            case STRING -> {
                return new StringRowCell(columnIndex, CellDataType.from(dataType), cell.getStringCellValue());
            }
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new LocalDateRowCell(columnIndex, CellDataType.from(dataType), cell.getDateCellValue());
                }
                return new NumericRowCell(columnIndex, CellDataType.from(dataType), cell.getNumericCellValue());
            }
            case BOOLEAN -> {
                return new BooleanRowCell(columnIndex, CellDataType.from(dataType), cell.getBooleanCellValue());
            }
            case BLANK -> {
                return new BlankRowCell(columnIndex, CellDataType.from(dataType), StringUtils.EMPTY);
            }
            default -> throw new ExcelFileProcessingException(DomainError.ERROR_EXCEL_CELL_DATA_TYPE);
        }
    }
}
