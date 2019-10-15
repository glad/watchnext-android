package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.implementation.service.SystemLogService;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.model.movie.MovieDetailed;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class GetMovieDetailUseCaseTest {
    private static final LogService LOG = SystemLogService.INSTANCE;
    private static final MovieDetailed MOVIE_DETAILED;

    static {
        try {
            MOVIE_DETAILED = MovieDetailed.newBuilder()
                    .id("id")
                    .title("title")
                    .overview("overview")
                    .posterImages(Arrays.asList(
                            Image.newBuilder().url("image1").width(111).height(222).build(),
                            Image.newBuilder().url("image2").width(333).height(444).build()))
                    .backdropImages(Arrays.asList(
                            Image.newBuilder().url("image1").width(555).height(666).build(),
                            Image.newBuilder().url("image2").width(777).height(888).build()))
                    .releaseDate(new GregorianCalendar().getTime())
                    .certification("certification")
                    .countryCode("CA")
                    .runtimeMinutes(1)
                    .genres(new ArrayList<>())
                    .build();
        } catch (final InvalidMovieException | InvalidImageException e) {
            throw new RuntimeException(e);
        }
    }

    @Mock private DataProvider mockDataProvider;

    private GetMovieDetailUseCase sut;

    @Before
    public void setup() {
        sut = new GetMovieDetailUseCase(mockDataProvider, LOG);
    }

    @Test
    public void execute_WhenMoreOrLessThanOneArgumentsSupplied_ResultInInvalidArgumentsException() {
        final TestObserver<MovieDetailed> observer = TestObserver.create();

        sut.execute().subscribe(observer);

        observer.assertError(InvalidArgumentsException.class);
    }

    @Test
    public void execute_OnError_InvalidMovieExceptionReturned() {
        final TestObserver<MovieDetailed> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieDetail(Mockito.anyString()))
                .thenReturn(Single.error(new InvalidMovieException(new Exception("Dummy"))));
        sut.execute(MOVIE_DETAILED.getId()).subscribe(observer);

        observer.assertError(InvalidMovieException.class);
    }

    @Test
    public void execute_OnSuccess_MovieDetailedSingleReturnedOnSubscribe() {
        final TestObserver<MovieDetailed> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieDetail(MOVIE_DETAILED.getId()))
                .thenReturn(Single.just(MOVIE_DETAILED));
        sut.execute(MOVIE_DETAILED.getId()).subscribe(observer);

        observer.onSuccess(MOVIE_DETAILED);
    }

    @Test
    public void execute_OnNever_SingleNeverReturnedOnSubscribe() {
        final TestObserver<MovieDetailed> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieDetail(MOVIE_DETAILED.getId()))
                .thenReturn(Single.never());
        sut.execute(MOVIE_DETAILED.getId()).subscribe(observer);

        observer.assertNever(MOVIE_DETAILED);
        observer.assertEmpty();
    }
}