package com.glad.watchnext.app.view.model.movie;

import com.glad.watchnext.domain.exception.InvalidMovieException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class MovieSimplifiedPresentationModelTest {

    @Test (expected = InvalidMovieException.class)
    public void build_WithMissingData_InvalidMovieExceptionThrown() throws InvalidMovieException {
        MovieSimplifiedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithOnlyRequiredData_ModelBuiltWithoutError() throws InvalidMovieException {
        MovieSimplifiedPresentationModel sut = MovieSimplifiedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getTitle(), is("title"));
        assertThat(sut.getOverview(), is("overview"));
    }

    @Test
    public void build_WithPartialOptionalData_ModelBuiltWithDefaultValues() throws InvalidMovieException {
        MovieSimplifiedPresentationModel sut = MovieSimplifiedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .build();

        assertThat(sut.getPosterImageUrl(), is(""));
        assertThat(sut.getBackdropImageUrl(), is(""));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidMovieException {
        MovieSimplifiedPresentationModel sut = MovieSimplifiedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .posterImageUrl("posterImageUrl")
                .backdropImageUrl("backdropImageUrl")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getTitle(), is("title"));
        assertThat(sut.getOverview(), is("overview"));
        assertThat(sut.getPosterImageUrl(), is("posterImageUrl"));
        assertThat(sut.getBackdropImageUrl(), is("backdropImageUrl"));
    }
}