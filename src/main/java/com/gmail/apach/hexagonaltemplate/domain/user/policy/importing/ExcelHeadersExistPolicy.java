package com.gmail.apach.hexagonaltemplate.domain.user.policy.importing;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import com.gmail.apach.hexagonaltemplate.domain.excel.vo.RowCell;
import com.gmail.apach.hexagonaltemplate.domain.user.constant.ImportUsersExcelHeaders;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class ExcelHeadersExistPolicy extends AbstractPolicy<ExcelBook> {

    @Override
    public boolean isSatisfiedWith(@NonNull ExcelBook context) {
        final var rows = context.getSheets().getFirst().getRows();
        boolean satisfied = CollectionUtils.isNotEmpty(rows)
            && CollectionUtils.isNotEmpty(rows.getFirst().getCells())
            && ImportUsersExcelHeaders.existAndNoDuplicates(
            rows.getFirst().getCells().stream()
                .map(RowCell::getValue)
                .map(Object::toString)
                .toList());
        terminate(satisfied);
        return satisfied;
    }
}
