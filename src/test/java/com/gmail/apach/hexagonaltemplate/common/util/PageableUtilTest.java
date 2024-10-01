package com.gmail.apach.hexagonaltemplate.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PageableUtilTest {

    private static final Integer PAGE = 1;
    private static final Integer SIZE = 10;
    private static final String SORT_ORDER = "sendBy";

    @InjectMocks
    private PageableUtil pageableUtil;

    @Test
    void obtainPageable_success() {
        final var sort = new String[]{SORT_ORDER};

        final var actual = pageableUtil.obtainPageable(PAGE, SIZE, sort);

        assertNotNull(actual);
        assertEquals(PAGE - 1, actual.getPageNumber());
        assertEquals(SIZE, actual.getPageSize());
        assertNotNull(actual.getSort());
        assertFalse(actual.getSort().isEmpty());

        Sort.Order sortOrder = actual.getSort().getOrderFor(SORT_ORDER);

        assertNotNull(sortOrder);
        assertEquals(Sort.Direction.ASC, sortOrder.getDirection());
    }

    @Test
    void obtainPageable_parametersNotDefined_fail() {
        assertThrows(NullPointerException.class, () -> pageableUtil.obtainPageable(null, SIZE, null));
        assertThrows(NullPointerException.class, () -> pageableUtil.obtainPageable(PAGE, null, null));
    }
}