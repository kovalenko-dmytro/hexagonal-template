package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.wrapper;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.common.BaseFilterWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class GetFilesFilterWrapper extends BaseFilterWrapper {

    private String fileName;
    private LocalDate createdFrom;
    private LocalDate createdTo;
}
