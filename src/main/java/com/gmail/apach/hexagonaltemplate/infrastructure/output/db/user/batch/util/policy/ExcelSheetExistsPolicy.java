package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.excel.model.ExcelBook;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class ExcelSheetExistsPolicy extends AbstractPolicy<ExcelBook> {

    public static final String SHEET_TITLE = "USERS";

    @Override
    public boolean isSatisfiedWith(@NonNull ExcelBook context) {
        boolean satisfied = CollectionUtils.isNotEmpty(context.getSheets())
            && context.getSheets().getFirst().getTitle().contentEquals(SHEET_TITLE);
        terminate(satisfied);
        return satisfied;
    }
}
