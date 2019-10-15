package com.glad.watchnext.domain.model.person;

import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.model.common.Image;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class PersonSimplifiedTest {
    //region Dummies
    /**
     * See {@link com.glad.watchnext.domain.model.common.ImageTest} for validation of {@link Image}
     */
    private static final Image DUMMY_IMAGE;

    static {
        try {
            DUMMY_IMAGE = Image.newBuilder()
                    .url("url")
                    .build();
        } catch (final InvalidImageException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion Dummies

    @Test (expected = InvalidPersonException.class)
    public void build_WithMissingData_InvalidPersonExceptionThrown() throws InvalidPersonException {
        PersonSimplified.newBuilder()
                .build();
    }

    @Test
    public void build_WithPartialRequiredData_ModelBuiltWithDefaultValues() throws InvalidPersonException {
        final PersonSimplified sut = PersonSimplified.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getProfileImage(), nullValue());
        assertThat(sut.getKnownFor(), nullValue());
    }

    @Test
    public void build_WithEmptyData_ModelBuiltWithNullValues() throws InvalidPersonException {
        final PersonSimplified sut = PersonSimplified.newBuilder()
                .id("id")
                .name("name")
                .profileImage(null)
                .knownFor(null)
                .build();

        assertThat(sut.getProfileImage(), nullValue());
        assertThat(sut.getKnownFor(), nullValue());
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidPersonException {
        final PersonSimplified sut = PersonSimplified.newBuilder()
                .id("id")
                .name("name")
                .profileImage(DUMMY_IMAGE)
                .knownFor(Arrays.asList("Movie", "Tv Show"))
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getProfileImage(), is(DUMMY_IMAGE));
        assertThat(sut.getKnownFor(), notNullValue());
        assertThat(sut.getKnownFor().size(), is(2));
        assertThat(sut.getKnownFor().get(0), is("Movie"));
        assertThat(sut.getKnownFor().get(1), is("Tv Show"));
    }
}