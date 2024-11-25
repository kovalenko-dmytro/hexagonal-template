package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.common.wrapper;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Data
public class BaseFilterWrapper {

    private final Integer page;
    private final Integer size;
    private final String[] sort;

    public Pageable toPageable() {
        Objects.requireNonNull(page);
        Objects.requireNonNull(size);
        return Objects.isNull(sort) || sort.length == 0
            ? PageRequest.of(page - 1, size)
            : PageRequest.of(page - 1, size, Sort.by(defineOrders(sort)));
    }

    private List<Sort.Order> defineOrders(String[] sort) {
        return Arrays.stream(sort)
            .map(sortOrder -> {
                final var _sort = Arrays.asList(sortOrder.split(CommonConstant.SPACE.getValue()));
                if (CollectionUtils.isEmpty(_sort)) {
                    return null;
                }
                if (_sort.size() == 1) {
                    return new Sort.Order(Sort.Direction.ASC, _sort.get(0));
                }
                final var order = _sort.get(1).equalsIgnoreCase(CommonConstant.DESC.getValue())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
                return new Sort.Order(order, _sort.get(0));
            })
            .filter(Objects::nonNull)
            .toList();
    }
}
