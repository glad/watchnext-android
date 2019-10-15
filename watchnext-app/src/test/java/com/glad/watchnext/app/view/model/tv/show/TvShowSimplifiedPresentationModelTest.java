package com.glad.watchnext.app.view.model.tv.show;

import com.glad.watchnext.domain.exception.InvalidTvShowException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class TvShowSimplifiedPresentationModelTest {

    @Test (expected = InvalidTvShowException.class)
    public void build_WithMissingData_InvalidTvShowExceptionThrown() throws InvalidTvShowException {
        TvShowSimplifiedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithOnlyRequiredData_ModelBuiltWithoutError() throws InvalidTvShowException {
        TvShowSimplifiedPresentationModel sut = TvShowSimplifiedPresentationModel.newBuilder()
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
        TvShowSimplifiedPresentationModel sut = TvShowSimplifiedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .build();

        assertThat(sut.getPosterImageUrl(), is(""));
        assertThat(sut.getBackdropImageUrl(), is(""));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidTvShowException {
        TvShowSimplifiedPresentationModel sut = TvShowSimplifiedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .overview("overview")
                .posterImageUrl("posterImageUrl")
                .backdropImageUrl("backdropImageUrl")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getOverview(), is("overview"));
        assertThat(sut.getPosterImageUrl(), is("posterImageUrl"));
        assertThat(sut.getBackdropImageUrl(), is("backdropImageUrl"));
    }
}