package com.gmail.apach.hexagonaltemplate.domain.excel.vo;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class SheetRow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private Integer index;
    private List<RowCell> cells;
}
