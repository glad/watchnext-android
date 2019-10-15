package com.glad.watchnext.app.view.home;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class HomePresenter implements HomeContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(HomePresenter.class);

    @NonNull private final LogService log;

    private HomeContract.View view;

    public HomePresenter(@NonNull final LogService log) {
        this.log = log;
    }

    //region ScreenContract.Presenter
    @Override
    public void bind(@NonNull final HomeContract.View view, @NonNull final Serializable... args) {
        log.d(TAG, "bind() called with: view = [" + view + "], args = [" + args.length + "]");
        this.view = view;
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
    }
    //endregion ScreenContract.Presenter

    //region HomeContract.Presenter
    @Override
    public void onMoviesClicked() {
        log.d(TAG, "onMoviesClicked() called");
        view.showMovies();
    }

    @Override
    public void onTvShowsClicked() {
        log.d(TAG, "onTvShowsClicked() called");
        view.showTvShows();
    }

    @Override
    public void onPeopleClicked() {
        log.d(TAG, "onPeopleClicked() called");
        view.showPeople();
    }
    //endregion HomeContract.Presenter
}