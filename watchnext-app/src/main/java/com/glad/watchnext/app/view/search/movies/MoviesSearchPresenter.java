package com.glad.watchnext.app.view.search.movies;

import com.glad.watchnext.app.usecase.search.GetPresentationMoviesSearchUseCase;
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
public final class MoviesSearchPresenter implements MoviesSearchContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(MoviesSearchPresenter.class);

    private MoviesSearchContract.View view;
    @NonNull private String searchQuery = "";

    @NonNull private final GetPresentationMoviesSearchUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NavigationService navigationService;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private int loadedCount = 0;

    public MoviesSearchPresenter(
            @NonNull final GetPresentationMoviesSearchUseCase useCase,
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
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        disposables.add(networkStateProvider.asObservable()
                .filter(state -> (state == NetworkConnectionState.CONNECTED && loadedCount == 0))
                .subscribe(ignore -> loadData(false)));

        if (loadedCount > 0) {
            log.w(TAG, "subscribe: Data already loaded :: loadedCount = [" + loadedCount + "]");
            return;
        }

        loadData(false);
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        disposables.clear();
    }

    @Override
    public void bind(@NonNull final MoviesSearchContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        searchQuery = args[0].toString();
    }
    //region ViewPresenterContract.Presenter

    //region MoviesSearchContract.Presenter
    @Override
    public void loadData(final boolean ignoreCache) {
        log.d(TAG, "loadData() called with: ignoreCache = [" + ignoreCache + "]");
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(ignoreCache && networkConnected, searchQuery, 1)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(ignore -> {
                    loadedCount = 0;
                    view.setEmptyContentVisibility(false);
                    view.onLoadStarted();
                })
                .doOnNext(ignore -> ++loadedCount)
                .subscribe(view::onLoaded,
                        throwable -> {
                            view.setEmptyContentVisibility(true);
                            view.onError(throwable);
                        },
                        () -> {
                            view.setEmptyContentVisibility(loadedCount == 0);
                            view.onLoadComplete();
                        }));
    }

    @Override
    public void onClicked(@NonNull final String id) {
        log.d(TAG, "onClicked() called with: id = [" + id + "]");
        navigationService.navigateToMovieDetail(id);
    }
    //endregion MoviesSearchContract.Presenter
}