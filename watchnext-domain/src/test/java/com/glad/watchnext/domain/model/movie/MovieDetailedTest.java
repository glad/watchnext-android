package com.glad.watchnext.domain.model.movie;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidGenreException;
import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.model.common.Genre;
import com.glad.watchnext.domain.model.common.Image;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class MovieDetailedTest {
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

    /**
     * See {@link com.glad.watchnext.domain.model.common.GenreTest} for validation of {@link Genre}
     */
    private static final Genre DUMMY_GENRE;

    static {
        try {
            DUMMY_GENRE = Genre.newBuilder()
                    .id("id")
                    .name("name")
                    .build();
        } catch (final InvalidGenreException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion Dummies

    @Test (expected = InvalidMovieException.class)
    public void build_WithMissingData_InvalidMovieExceptionThrown() throws InvalidMovieException {
        MovieDetailed.newBuilder()
                .build();
    }

    @Test (expected = InvalidMovieException.class)
    public void build_WithBadCountryCode_InvalidMovieExceptionThrown() throws InvalidMovieException {
        MovieDetailed.newBuilder()
                .id("id")
                .title("title")
                .countryCode("CANADA")
                .build();
    }

    @Test
    public void build_WithPartialRequiredData_ModelBuiltWithDefaultValues() throws InvalidMovieException {
        final MovieDetailed sut = MovieDetailed.newBuilder()
                .id("id")
                .title("title")
                .build();

        assertThat(sut.getPosterImages(), nullValue());
        assertThat(sut.getBackdropImages(), nullValue());
        assertThat(sut.getOverview(), is(ModelDefaults.MOVIE_MISSING_OVERVIEW));
        assertThat(sut.getReleaseDate(), nullValue());
        assertThat(sut.getRuntimeMinutes(), is(0L));
        assertThat(sut.getCertification(), is(ModelDefaults.MOVIE_MISSING_CERTIFICATION));
        assertThat(sut.getCountryCode(), nullValue());
        assertThat(sut.getGenres(), nullValue());
    }

    @Test
    public void build_WithEmptyData_ModelBuiltWithNullOrDefaultValues() throws InvalidMovieException {
        final MovieDetailed sut = MovieDetailed.newBuilder()
                .id("id")
                .title("title")
                .runtimeMinutes(-1L)
                .certification("")
                .countryCode("")
                .build();

        assertThat(sut.getRuntimeMinutes(), is(0L));
        assertThat(sut.getCertification(), is(ModelDefaults.MOVIE_MISSING_CERTIFICATION));
        assertThat(sut.getCountryCode(), nullValue());
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidMovieException {
        final Date now = new GregorianCalendar().getTime();
        final MovieDetailed sut = MovieDetailed.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .posterImages(Arrays.asList(DUMMY_IMAGE, DUMMY_IMAGE))
                .backdropImages(Arrays.asList(DUMMY_IMAGE, DUMMY_IMAGE))
                .releaseDate(now)
                .certification("certification")
                .countryCode("CA")
                .runtimeMinutes(1L)
                .genres(Arrays.asList(DUMMY_GENRE, DUMMY_GENRE))
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getTitle(), is("title"));
        assertThat(sut.getOverview(), is("overview"));
        assertThat(sut.getPosterImages().size(), is(2));
        assertThat(sut.getBackdropImages().size(), is(2));
        assertThat(sut.getReleaseDate(), is(now));
        assertThat(sut.getCertification(), is("certification"));
        assertThat(sut.getCountryCode(), is("CA"));
        assertThat(sut.getRuntimeMinutes(), is(1L));
        assertThat(sut.getGenres().size(), is(2));
    }
}