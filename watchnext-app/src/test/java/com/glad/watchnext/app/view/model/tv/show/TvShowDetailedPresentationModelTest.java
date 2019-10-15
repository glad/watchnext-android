package com.glad.watchnext.app.view.model.tv.show;

import com.glad.watchnext.domain.exception.InvalidTvShowException;

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
public final class TvShowDetailedPresentationModelTest {

    @Test (expected = InvalidTvShowException.class)
    public void build_WithMissingData_InvalidTvShowExceptionThrown() throws InvalidTvShowException {
        TvShowDetailedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithOnlyRequiredData_ModelBuiltWithoutError() throws InvalidTvShowException {
        TvShowDetailedPresentationModel sut = TvShowDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getOverview(), is("overview"));
    }

    @Test
    public void build_WithPartialOptionalData_ModelBuiltWithDefaultValues() throws InvalidTvShowException {
        TvShowDetailedPresentationModel sut = TvShowDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .build();

        assertThat(sut.getPosterImageUrls().isEmpty(), is(true));
        assertThat(sut.getBackdropImageUrls().isEmpty(), is(true));
        assertThat(sut.getMetadataLine1(), is(""));
        assertThat(sut.getMetadataLine2(), is(""));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidTvShowException {
        TvShowDetailedPresentationModel sut = TvShowDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .posterImageUrls(Arrays.asList("poster1", "poster2"))
                .backdropImageUrls(Arrays.asList("backdrop1", "backdrop2"))
                .rating("PG-13")
                .countryCode("CA")
                .runtimeMs(60L)
                .network("CBS")
                .genres(Arrays.asList("Action", "Thriller"))
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getOverview(), is("overview"));
        assertThat(sut.getPosterImageUrls().size(), is(2));
        assertThat(sut.getPosterImageUrls().get(0), is("poster1"));
        assertThat(sut.getPosterImageUrls().get(1), is("poster2"));
        assertThat(sut.getBackdropImageUrls().size(), is(2));
        assertThat(sut.getBackdropImageUrls().get(0), is("backdrop1"));
        assertThat(sut.getBackdropImageUrls().get(1), is("backdrop2"));
        assertThat(sut.getMetadataLine1(), is("CBS • PG-13 (CA) • 60 min."));
        assertThat(sut.getMetadataLine2(), is("Action, Thriller"));
    }

    @Test
    public void build_WithMissingCountryCodeAndNetworkData_Metadata1BuiltWithSuppliedValues() throws InvalidTvShowException {
        TvShowDetailedPresentationModel sut = TvShowDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .rating("PG-13")
                .genres(new ArrayList<>())
                .build();

        assertThat(sut.getMetadataLine1(), is("PG-13"));
    }

    @Test
    public void build_WithMissingReleaseYearAndRatingData_Metadata1BuiltWithSuppliedValues() throws InvalidTvShowException {
        TvShowDetailedPresentationModel sut = TvShowDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .runtimeMs(60L)
                .countryCode("CA")
                .genres(new ArrayList<>())
                .build();

        assertThat(sut.getMetadataLine1(), is("60 min."));
    }
}