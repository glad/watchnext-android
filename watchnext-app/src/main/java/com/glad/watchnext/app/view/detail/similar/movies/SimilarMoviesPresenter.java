package com.glad.watchnext.app.view.detail.similar.movies;

import com.glad.watchnext.app.usecase.movie.GetPresentationSimilarMoviesUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.NetworkStateProvider.NetworkConnectionState;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;

import android.support.annotation.NonNull;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class SimilarMoviesPresenter implements SimilarMoviesContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(SimilarMoviesPresenter.class);

    private SimilarMoviesContract.View view;
    @NonNull private String movieId = "";

    @NonNull private final GetPresentationSimilarMoviesUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NavigationService navigationService;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private int loadedCount = 0;

    public SimilarMoviesPresenter(
            @NonNull final GetPresentationSimilarMoviesUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.schedulerProvider = schedulerProvider;
        this.navigationService = navigationService;
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ViewPresenterContract.Presenter

    @Override
    public void bind(@NonNull final SimilarMoviesContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        movieId = args[0].toString();
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        disposables.add(networkStateProvider.asObservable()
                .filter(state -> (state == NetworkConnectionState.CONNECTED && loadedCount == 0))
                .subscribe(ignore -> loadData()));

        if (loadedCount > 0) {
            log.w(TAG, "subscribe: Data already loaded");
            return;
        }

        loadData();
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        disposables.clear();
    }
    //region ViewPresenterContract.Presenter

    //region SimilarMoviesContract.Presenter
    @Override
    public void loadData() {
        log.d(TAG, "loadData() called");
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(networkConnected, movieId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(ignore -> {
                    loadedCount = 0;
                    view.onLoadStarted();
                })
                .doOnNext(ignore -> ++loadedCount)
                .subscribe(view::onLoaded, view::onError, () -> {
                    view.onLoadComplete();
                    view.setVisibility(loadedCount > 0);
                }));
    }

    @Override
    public void onClicked(@NonNull final String id) {
        log.d(TAG, "onClicked() called with: id = [" + id + "]");
        navigationService.navigateToMovieDetail(id);
    }
    //endregion SimilarMoviesContract.Presenter
}