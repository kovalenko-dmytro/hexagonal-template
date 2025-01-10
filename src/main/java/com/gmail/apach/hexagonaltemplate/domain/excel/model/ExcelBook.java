package com.gmail.apach.hexagonaltemplate.domain.excel.model;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.ExcelFileProcessingException;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.BookSheet;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.ExcelFileExtension;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.RowCell;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.SheetRow;
import com.gmail.apach.hexagonaltemplate.domain.user.constant.ImportUsersExcelHeaders;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.OperationNotSupportedException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class ExcelBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private final ExcelFileExtension extension;
    private final List<BookSheet> sheets = new ArrayList<>();

    public ExcelBook(byte[] bytes, ExcelFileExtension extension) {
        final var workbook = extractWorkBook(bytes, extension);
        this.extension = extension;
        fillInSheets(workbook);
    }

    private Workbook extractWorkBook(byte[] bytes, ExcelFileExtension extension) {
        try (var ba = new ByteArrayInputStream(bytes)) {
            switch (extension) {
                case HSSF -> {
                    return new HSSFWorkbook(ba);
                }
                case XSSF -> {
                    return new XSSFWorkbook(ba);
                }
                default -> throw new OperationNotSupportedException();
            }
        } catch (IOException e) {
            throw new ExcelFileProcessingException(DomainError.ERROR_READ_EXCEL_FILE);
        } catch (OperationNotSupportedException e) {
            throw new ExcelFileProcessingException(DomainError.ERROR_EXCEL_FILE_EXTENSION, new Object[]{extension});
        }
    }

    private void fillInSheets(Workbook workbook) {
        for (var workBookSheet : workbook) {
            sheets.add(
                BookSheet.builder()
                    .title(workBookSheet.getSheetName())
                    .rows(fillInRows(workBookSheet))
                    .build()
            );
        }
    }

    private List<SheetRow> fillInRows(Sheet sheet) {
        return IntStream.rangeClosed(sheet.getFirstRowNum(), sheet.getLastRowNum())
            .mapToObj(index ->
                SheetRow.builder()
                    .index(index)
                    .cells(fillInSells(sheet.getRow(index)))
                    .build())
            .toList();
    }

    private List<RowCell> fillInSells(Row row) {
        return IntStream
            .range(0, ImportUsersExcelHeaders.headers.size())
            .mapToObj(index -> RowCell.getInstance(index, row.getCell(index)))
            .toList();
    }
}
