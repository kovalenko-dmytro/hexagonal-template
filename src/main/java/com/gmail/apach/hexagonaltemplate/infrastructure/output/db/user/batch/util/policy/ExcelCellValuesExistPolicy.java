package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.CellDataType;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

import java.util.Objects;

@NoArgsConstructor
public class ExcelCellValuesExistPolicy extends AbstractPolicy<ExcelBook> {

    @Override
    public boolean isSatisfiedWith(@NonNull ExcelBook context) {
        final var rows = context.getSheets().getFirst().getRows().stream().skip(1).toList();
        final var satisfied = CollectionUtils.isNotEmpty(rows)
            && rows.stream().allMatch(row -> row.getCells().stream()
                .allMatch(rowCell ->
                    Objects.nonNull(rowCell.getValue()) && !rowCell.getDataType().equals(CellDataType.BLANK)));
        terminate(satisfied);
        return satisfied;
    }
}
