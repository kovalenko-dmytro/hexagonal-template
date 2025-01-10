package com.gmail.apach.hexagonaltemplate.domain.excel.vo.cell;

import com.gmail.apach.hexagonaltemplate.domain.excel.vo.CellDataType;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.RowCell;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class LocalDateRowCell extends RowCell implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private LocalDate value;

    public LocalDateRowCell(Integer index, CellDataType dataType, Date value) {
        super(index, dataType);
        this.value = LocalDate.ofInstant(value.toInstant(), ZoneId.systemDefault());
    }
}
