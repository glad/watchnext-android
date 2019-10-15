package com.glad.watchnext.app.view.detail.similar.tv.shows;

import com.glad.watchnext.app.usecase.tv.show.GetPresentationSimilarTvShowsUseCase;
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
public final class SimilarTvShowsPresenter implements SimilarTvShowsContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(SimilarTvShowsPresenter.class);

    private SimilarTvShowsContract.View view;
    @NonNull private String tvShowId = "";

    @NonNull private final GetPresentationSimilarTvShowsUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NavigationService navigationService;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private int loadedCount = 0;

    public SimilarTvShowsPresenter(
            @NonNull final GetPresentationSimilarTvShowsUseCase useCase,
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
    public void bind(@NonNull final SimilarTvShowsContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        tvShowId = args[0].toString();
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

    //region SimilarTvShowsContract.Presenter
    @Override
    public void loadData() {
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(networkConnected, tvShowId)
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
        navigationService.navigateToTvShowDetail(id);
    }
    //endregion SimilarTvShowsContract.Presenter
}