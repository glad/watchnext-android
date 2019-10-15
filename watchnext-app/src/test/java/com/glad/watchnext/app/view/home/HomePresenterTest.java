package com.glad.watchnext.app.view.home;

import com.glad.watchnext.domain.implementation.service.EmptyLogService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by Gautam Lad
 */
@RunWith (MockitoJUnitRunner.class)
public final class HomePresenterTest {

    @Mock private HomeContract.View mockView;

    private HomeContract.Presenter sut;

    @Before
    public void setup() {
        sut = new HomePresenter(EmptyLogService.INSTANCE);
        sut.bind(mockView);
    }

    @Test
    public void onMoviesClicked_OnInteraction_viewShowMoviesCalled() {
        sut.onMoviesClicked();
        Mockito.verify(mockView, Mockito.times(1)).showMovies();
    }

    @Test
    public void onTvShowsClicked_OnInteraction_viewShowTvShowsCalled() {
        sut.onTvShowsClicked();
        Mockito.verify(mockView, Mockito.times(1)).showTvShows();
    }

    @Test
    public void onPeopleClicked_OnInteraction_viewShowPeopleCalled() {
        sut.onPeopleClicked();
        Mockito.verify(mockView, Mockito.times(1)).showPeople();
    }
}