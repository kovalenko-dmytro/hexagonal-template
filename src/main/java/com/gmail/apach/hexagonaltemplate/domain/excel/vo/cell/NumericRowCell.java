package com.gmail.apach.hexagonaltemplate.domain.excel.vo.cell;

import com.gmail.apach.hexagonaltemplate.domain.excel.vo.CellDataType;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.RowCell;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class NumericRowCell extends RowCell implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private Double value;

    public NumericRowCell(Integer index, CellDataType dataType, Double value) {
        super(index, dataType);
        this.value = value;
    }
}
