package com.glad.watchnext.app.view.home.people;

import com.glad.watchnext.app.usecase.person.GetPresentationPeopleCategoriesUseCase;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;
import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.implementation.provider.TestSchedulerProvider;
import com.glad.watchnext.domain.implementation.service.SystemLogService;
import com.glad.watchnext.domain.provider.NetworkStateProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class PeoplePresenterTest {

    @Mock private PeopleContract.View mockView;
    @Mock private GetPresentationPeopleCategoriesUseCase mockUseCase;
    @Mock private NetworkStateProvider mockNetworkStateProvider;

    private PeopleContract.Presenter sut;

    private static CollectionPresentationModel COLLECTION;

    static {
        try {
            COLLECTION = CollectionPresentationModel.newBuilder().id("id").name("name").build();
        } catch (final InvalidCategoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setup() {
        sut = new PeoplePresenter(mockUseCase, TestSchedulerProvider.INSTANCE, mockNetworkStateProvider, SystemLogService.INSTANCE);
        sut.bind(mockView);
        Mockito.when(mockNetworkStateProvider.asObservable()).thenReturn(Observable.empty());
        Mockito.when(mockUseCase.execute(false)).thenReturn(Observable.empty());
    }

    @Test
    public void subscribe_executeUseCase_callViewOnLoadStartedAndViewOnLoadComplete() {
        sut.subscribe();
        Mockito.verify(mockView).setEmptyContentVisibility(false);
        Mockito.verify(mockView).setEmptyContentVisibility(true);
        Mockito.verify(mockView).onLoadStarted();
        Mockito.verify(mockView).onLoadComplete();
        Mockito.verifyNoMoreInteractions(mockView);
    }

    @Test
    public void loadData_executeUseCase_onErrorCallViewOnError() {
        final Throwable error = new InvalidCategoryException(new Exception("Error"));
        Mockito.when(mockUseCase.execute(Mockito.anyBoolean())).thenReturn(Observable.error(error));
        sut.loadData();
        Mockito.verify(mockView).onError(error);
    }

    @Test
    public void loadData_executeUseCase_callViewOnCollectionLoadedAndEmptyContentVisibilityTwice() {
        Mockito.when(mockUseCase.execute(Mockito.anyBoolean())).thenReturn(Observable.just(COLLECTION));
        sut.loadData();
        Mockito.verify(mockView).onCollectionLoaded(COLLECTION);
        Mockito.verify(mockView, Mockito.times(2)).setEmptyContentVisibility(false);
    }
}