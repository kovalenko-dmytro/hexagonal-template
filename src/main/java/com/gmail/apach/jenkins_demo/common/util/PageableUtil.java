package com.gmail.apach.jenkins_demo.common.util;

import com.gmail.apach.jenkins_demo.common.constant.CommonConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class PageableUtil {

    public Pageable obtainPageable(Integer page, Integer size, String[] sort) {
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
