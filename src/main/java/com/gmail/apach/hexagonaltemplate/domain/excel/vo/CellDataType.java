package com.gmail.apach.hexagonaltemplate.domain.excel.vo;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.ExcelFileProcessingException;
import org.apache.poi.ss.usermodel.CellType;

public enum CellDataType {
    NONE, NUMERIC, STRING, FORMULA, BLANK, BOOLEAN, ERROR;

    public static CellDataType from(CellType cellType) {
        switch (cellType) {
            case ERROR -> {
                return CellDataType.ERROR;
            }
            case BLANK -> {
                return CellDataType.BLANK;
            }
            case STRING -> {
                return CellDataType.STRING;
            }
            case BOOLEAN -> {
                return CellDataType.BOOLEAN;
            }
            case NUMERIC -> {
                return CellDataType.NUMERIC;
            }
            case FORMULA -> {
                return CellDataType.FORMULA;
            }
            case _NONE -> {
                return CellDataType.NONE;
            }
            default ->
                throw new ExcelFileProcessingException(DomainError.ERROR_EXCEL_FILE_EXTENSION, new Object[]{cellType});
        }
    }
}
