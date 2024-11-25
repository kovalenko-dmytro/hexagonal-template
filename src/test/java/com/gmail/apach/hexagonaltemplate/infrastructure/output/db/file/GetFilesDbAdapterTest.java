package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class GetFilesDbAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private GetFilesDbAdapter getFilesDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_withoutFilter() {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .page(1)
            .size(5)
            .sort(new String[]{"created"})
            .build();

        final var actual = getFilesDbAdapter.get(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new StoredFile[0]), Comparator.comparing(StoredFile::getCreated)));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_searchByFileName() {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .fileName("file4")
            .page(1)
            .size(5)
            .build();

        final var actual = getFilesDbAdapter.get(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_searchByCreatedFrom() {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 19))
            .page(1)
            .size(5)
            .build();

        final var actual = getFilesDbAdapter.get(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_searchByCreatedFromAndCreatedTo() {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 23))
            .createdTo(LocalDate.of(2024, 8, 25))
            .page(1)
            .size(5)
            .build();

        final var actual = getFilesDbAdapter.get(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(3, actual.getContent().size());
        assertEquals(3, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_sortByCreatedDesc() {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .sort(new String[]{"created desc"})
            .page(1)
            .size(5)
            .build();

        final var actual = getFilesDbAdapter.get(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new StoredFile[0]), Comparator.comparing(StoredFile::getCreated).reversed()));
    }
}