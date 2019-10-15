package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.implementation.service.SystemLogService;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class GetMoviesFromCategoryUseCaseTest {
    private static final LogService LOG = SystemLogService.INSTANCE;
    private static final String COLLECTION_ID = "id";
    private static final int PAGE_INDEX = 1;

    private static final MovieSimplified MOVIE_SIMPLIFIED;

    static {
        try {
            MOVIE_SIMPLIFIED = MovieSimplified.newBuilder()
                    .id("id")
                    .title("title")
                    .overview("overview")
                    .posterImage(Image.newBuilder().url("posterImage").width(111).height(222).build())
                    .backdropImage(Image.newBuilder().url("backdropImage").width(333).height(444).build())
                    .build();
        } catch (final InvalidMovieException | InvalidImageException e) {
            throw new RuntimeException(e);
        }
    }

    @Mock private DataProvider mockDataProvider;

    private GetMoviesFromCategoryUseCase sut;

    @Before
    public void setup() {
        sut = new GetMoviesFromCategoryUseCase(mockDataProvider, LOG);
    }

    @Test
    public void execute_WhenMoreOrLessThanTwoArgumentsSupplied_ResultInInvalidArgumentsException() {
        final TestObserver<MovieSimplified> observer = TestObserver.create();

        sut.execute().subscribe(observer);

        observer.assertError(InvalidArgumentsException.class);
    }

    @Test
    public void execute_WhenBadArgumentsSupplied_ResultInInvalidArgumentsException() {
        final TestObserver<MovieSimplified> observer = TestObserver.create();

        sut.execute(PAGE_INDEX, COLLECTION_ID).subscribe(observer);

        observer.assertError(InvalidArgumentsException.class);
    }

    @Test
    public void execute_ReturnItemsEvenIfSomeFailFromDataProvider_ListOfMovieSimplifiedResulted() {
        Mockito.when(mockDataProvider.fetchMoviesFromCategoryId(COLLECTION_ID, PAGE_INDEX))
                .thenReturn(Observable.fromArray(MOVIE_SIMPLIFIED, MOVIE_SIMPLIFIED, null));
        List result = sut.execute(COLLECTION_ID, PAGE_INDEX)
                .toList()
                .blockingGet();

        assertThat(result.size(), is(2));
    }

    @Test
    public void execute_OnSuccess_MovieReturnedOnSubscribe() {
        final TestObserver<MovieSimplified> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMoviesFromCategoryId(COLLECTION_ID, PAGE_INDEX))
                .thenReturn(Observable.just(MOVIE_SIMPLIFIED));
        sut.execute(COLLECTION_ID, PAGE_INDEX).subscribe(observer);

        observer.onSuccess(MOVIE_SIMPLIFIED);
    }

    @Test
    public void execute_OnNever_ObservableNeverReturnedOnSubscribe() {
        final TestObserver<MovieSimplified> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMoviesFromCategoryId(COLLECTION_ID, PAGE_INDEX))
                .thenReturn(Observable.never());
        sut.execute(COLLECTION_ID, PAGE_INDEX).subscribe(observer);

        observer.assertNever(MOVIE_SIMPLIFIED);
        observer.assertEmpty();
    }
}