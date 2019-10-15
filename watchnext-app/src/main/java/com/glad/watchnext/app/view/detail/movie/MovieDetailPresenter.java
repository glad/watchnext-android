package com.glad.watchnext.app.view.detail.movie;

import com.glad.watchnext.app.usecase.movie.GetPresentationMovieDetailUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.NetworkStateProvider.NetworkConnectionState;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class MovieDetailPresenter implements MovieDetailContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(MovieDetailPresenter.class);

    private MovieDetailContract.View view;
    @NonNull private String movieId = "";

    @NonNull private final GetPresentationMovieDetailUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean isLoaded = false;

    public MovieDetailPresenter(
            @NonNull final GetPresentationMovieDetailUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.schedulerProvider = schedulerProvider;
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ViewPresenterContract.Presenter

    @Override
    public void bind(@NonNull final MovieDetailContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        movieId = args[0].toString();
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        disposables.add(networkStateProvider.asObservable()
                .filter(state -> (state == NetworkConnectionState.CONNECTED && !isLoaded))
                .subscribe(ignore -> loadData(false)));

        if (isLoaded) {
            log.w(TAG, "subscribe: Data already loaded");
            return;
        }

        loadData(false);
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        disposables.clear();
    }
    //region ViewPresenterContract.Presenter

    //region MovieDetailContract.Presenter
    @Override
    public void loadData(final boolean ignoreCache) {
        log.d(TAG, "loadData() called with: ignoreCache = [" + ignoreCache + "]");
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(ignoreCache && networkConnected, movieId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(ignore -> {
                    isLoaded = false;
                    view.setEmptyContentVisibility(false);
                    view.onLoadStarted();
                })
                .subscribe(view::onLoadComplete, throwable -> {
                    view.setEmptyContentVisibility(true);
                    view.onError(throwable);
                }));
    }
    //endregion MovieDetailContract.Presenter
}