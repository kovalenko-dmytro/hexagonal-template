package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.CellDataType;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class ExcelCellDataTypesIsBooleanPolicy extends AbstractPolicy<ExcelBook> {

    @Override
    public boolean isSatisfiedWith(@NonNull ExcelBook context) {
        final var rows = context.getSheets().getFirst().getRows().stream().skip(1).toList();
        final var satisfied = CollectionUtils.isNotEmpty(rows)
            && rows.stream().allMatch(row -> row.getCells().stream()
                .filter(rowCell -> rowCell.getIndex() == 5 || rowCell.getIndex() == 6)
                .allMatch(rowCell -> rowCell.getDataType().equals(CellDataType.BOOLEAN)));
        terminate(satisfied);
        return satisfied;
    }
}
