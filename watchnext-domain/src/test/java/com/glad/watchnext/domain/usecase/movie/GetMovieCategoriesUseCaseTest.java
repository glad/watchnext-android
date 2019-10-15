package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.implementation.service.SystemLogService;
import com.glad.watchnext.domain.model.common.Category;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class GetMovieCategoriesUseCaseTest {
    private static final LogService LOG = SystemLogService.INSTANCE;
    private static final Category CATEGORY;

    static {
        try {
            CATEGORY = Category.newBuilder()
                    .id("id")
                    .name("name").build();
        } catch (final InvalidCategoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Mock private DataProvider mockDataProvider;

    private GetMovieCategoriesUseCase sut;

    @Before
    public void setup() {
        sut = new GetMovieCategoriesUseCase(mockDataProvider, LOG);
    }

    @Test
    public void execute_OnError_EmptyCollectionReturned() {
        final TestObserver<Category> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieCategories())
                .thenReturn(Observable.error(new InvalidCategoryException(new Exception("Dummy"))));
        sut.execute().subscribe(observer);

        observer.assertNoValues();
        observer.assertComplete();
    }

    @Test
    public void execute_OnSuccess_CollectionObservableReturnedOnSubscribe() {
        final TestObserver<Category> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieCategories())
                .thenReturn(Observable.just(CATEGORY));
        sut.execute().subscribe(observer);

        observer.onSuccess(CATEGORY);
    }

    @Test
    public void execute_OnEmpty_EmptyObservableReturnedOnSubscribe() {
        final TestObserver<Category> observer = TestObserver.create();

        Mockito.when(mockDataProvider.fetchMovieCategories())
                .thenReturn(Observable.empty());
        sut.execute().subscribe(observer);

        observer.assertNever(CATEGORY);
        observer.assertComplete();
    }
}