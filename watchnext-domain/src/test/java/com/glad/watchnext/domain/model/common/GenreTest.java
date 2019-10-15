package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidGenreException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class GenreTest {

    @Test (expected = InvalidGenreException.class)
    public void build_WithMissingData_InvalidGenreExceptionThrown() throws InvalidGenreException {
        Genre.newBuilder()
                .build();
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidGenreException {
        final Genre sut = Genre.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
    }
}
