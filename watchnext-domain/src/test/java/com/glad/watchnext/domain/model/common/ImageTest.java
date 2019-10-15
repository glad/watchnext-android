package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidImageException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class ImageTest {

    @Test (expected = InvalidImageException.class)
    public void build_WithMissingData_InvalidImageExceptionThrown() throws InvalidImageException {
        Image.newBuilder()
                .build();
    }

    @Test
    public void build_WithSuppliedData_ModelBuiltWithSuppliedValues() throws InvalidImageException {
        final Image sut = Image.newBuilder()
                .url("url")
                .width(111)
                .height(222)
                .build();

        assertThat(sut.getUrl(), is("url"));
        assertThat(sut.getWidth(), is(111));
        assertThat(sut.getHeight(), is(222));
        assertThat(sut.getAspectRatio(), is((float) 111 / (float) 222));
    }

    @Test
    public void build_WithNegativeValues_ModelBuiltWithZeroValues() throws InvalidImageException {
        final Image sut = Image.newBuilder()
                .url("url")
                .width(-111)
                .height(-222)
                .build();

        assertThat(sut.getWidth(), is(0));
        assertThat(sut.getHeight(), is(0));
        assertThat(sut.getAspectRatio(), is(0.0f));
    }
}
