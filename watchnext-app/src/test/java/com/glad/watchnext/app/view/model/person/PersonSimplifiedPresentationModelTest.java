package com.glad.watchnext.app.view.model.person;

import com.glad.watchnext.domain.exception.InvalidPersonException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class PersonSimplifiedPresentationModelTest {

    @Test (expected = InvalidPersonException.class)
    public void build_WithMissingData_InvalidPersonExceptionThrown() throws InvalidPersonException {
        PersonSimplifiedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithOnlyRequiredData_ModelBuiltWithoutError() throws InvalidPersonException {
        PersonSimplifiedPresentationModel sut = PersonSimplifiedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
    }

    @Test
    public void build_WithPartialOptionalData_ModelBuiltWithDefaultValues() throws InvalidPersonException {
        PersonSimplifiedPresentationModel sut = PersonSimplifiedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getProfileImageUrl(), is(""));
        assertThat(sut.getKnownFor().isEmpty(), is(true));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidPersonException {
        PersonSimplifiedPresentationModel sut = PersonSimplifiedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .profileImageUrl("profileImageUrl")
                .knownFor(Arrays.asList("Movie", "Tv Show"))
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getKnownFor(), is("Movie, Tv Show"));
        assertThat(sut.getProfileImageUrl(), is("profileImageUrl"));
    }
}