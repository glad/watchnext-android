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
public final class PersonDetailedPresentationModelTest {

    @Test (expected = InvalidPersonException.class)
    public void build_WithMissingData_InvalidPersonExceptionThrown() throws InvalidPersonException {
        PersonDetailedPresentationModel.newBuilder()
                .build();
    }

    @Test
    public void build_WithOnlyRequiredData_ModelBuiltWithoutError() throws InvalidPersonException {
        PersonDetailedPresentationModel sut = PersonDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
    }

    @Test
    public void build_WithPartialOptionalData_ModelBuiltWithDefaultValues() throws InvalidPersonException {
        PersonDetailedPresentationModel sut = PersonDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .build();

        assertThat(sut.getProfileImageUrls().isEmpty(), is(true));
        assertThat(sut.getBiography(), is(""));
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidPersonException {
        PersonDetailedPresentationModel sut = PersonDetailedPresentationModel.newBuilder()
                .id("id")
                .name("name")
                .profileImageUrls(Arrays.asList("profile1", "profile2"))
                .backdropImageUrls(Arrays.asList("backdrop1", "backdrop2"))
                .biography("biography")
                .build();

        assertThat(sut.getId(), is("id"));
        assertThat(sut.getName(), is("name"));
        assertThat(sut.getBiography(), is("biography"));
        assertThat(sut.getProfileImageUrls().size(), is(2));
        assertThat(sut.getProfileImageUrls().get(0), is("profile1"));
        assertThat(sut.getProfileImageUrls().get(1), is("profile2"));
        assertThat(sut.getBackdropImageUrls().size(), is(2));
        assertThat(sut.getBackdropImageUrls().get(0), is("backdrop1"));
        assertThat(sut.getBackdropImageUrls().get(1), is("backdrop2"));
    }
}