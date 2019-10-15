package com.glad.watchnext.app.view.model.movie;

import com.glad.watchnext.domain.exception.InvalidMovieException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class MovieDetailedPresentationModelTest {

    @Test (expected = InvalidMovieException.class)
    public void build_WithMissingData_InvalidMovieExceptionThrown() throws InvalidMovieException {
        MovieDetailedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithPartialRequiredData_ModelBuiltWithDefaultValues() throws InvalidMovieException {
        MovieDetailedPresentationModel sut = MovieDetailedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .build();

        assertThat(sut.getPosterImageUrls().isEmpty(), is(true));
        assertThat(sut.getBackdropImageUrls().isEmpty(), is(true));
        assertThat(sut.getMetadataLine1(), is(""));
        assertThat(sut.getMetadataLine2(), is(""));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidMovieException {
        MovieDetailedPresentationModel sut = MovieDetailedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .posterImageUrls(Arrays.asList("poster1", "poster2"))
                .backdropImageUrls(Arrays.asList("backdrop1", "backdrop2"))
                .certification("PG-13")
                .countryCode("CA")
                .runtimeMs(60L)
                .releaseYear(2018)
                .genres(Arrays.asList("Action", "Thriller"))
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getTitle(), is("title"));
        assertThat(sut.getOverview(), is("overview"));
        assertThat(sut.getPosterImageUrls().size(), is(2));
        assertThat(sut.getPosterImageUrls().get(0), is("poster1"));
        assertThat(sut.getPosterImageUrls().get(1), is("poster2"));
        assertThat(sut.getBackdropImageUrls().size(), is(2));
        assertThat(sut.getBackdropImageUrls().get(0), is("backdrop1"));
        assertThat(sut.getBackdropImageUrls().get(1), is("backdrop2"));
        assertThat(sut.getMetadataLine1(), is("2018 • PG-13 (CA) • 60 min."));
        assertThat(sut.getMetadataLine2(), is("Action, Thriller"));
    }

    @Test
    public void build_WithMissingCountryCodeAndRuntimeData_Metadata1BuiltWithSuppliedValues() throws InvalidMovieException {
        MovieDetailedPresentationModel sut = MovieDetailedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .certification("PG-13")
                .releaseYear(2018)
                .genres(new ArrayList<>())
                .build();

        assertThat(sut.getMetadataLine1(), is("2018 • PG-13"));
    }

    @Test
    public void build_WithMissingReleaseYearAndCertificationData_Metadata1BuiltWithSuppliedValues() throws InvalidMovieException {
        MovieDetailedPresentationModel sut = MovieDetailedPresentationModel.newBuilder()
                .id("id")
                .title("title")
                .overview("overview")
                .runtimeMs(60L)
                .countryCode("CA")
                .genres(new ArrayList<>())
                .build();

        assertThat(sut.getMetadataLine1(), is("60 min."));
    }
}
