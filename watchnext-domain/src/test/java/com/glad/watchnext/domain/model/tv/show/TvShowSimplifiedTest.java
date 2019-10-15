package com.glad.watchnext.domain.model.tv.show;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.Image;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class TvShowSimplifiedTest {
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

    @Test (expected = InvalidTvShowException.class)
    public void build_WithMissingData_InvalidTvShowExceptionThrown() throws InvalidTvShowException {
        TvShowSimplified.newBuilder()
                .build();
    }

    @Test
    public void build_WithPartialRequiredData_ModelBuiltWithDefaultValues() throws InvalidTvShowException {
        final TvShowSimplified sut = TvShowSimplified.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getOverview(), is(ModelDefaults.TVSHOW_MISSING_OVERVIEW));
        assertThat(sut.getPosterImage(), nullValue());
        assertThat(sut.getBackdropImage(), nullValue());
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidTvShowException {
        final TvShowSimplified sut = TvShowSimplified.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .posterImage(DUMMY_IMAGE)
                .backdropImage(DUMMY_IMAGE)
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getPosterImage(), is(DUMMY_IMAGE));
        assertThat(sut.getBackdropImage(), is(DUMMY_IMAGE));
        assertThat(sut.getOverview(), is("overview"));
    }
}