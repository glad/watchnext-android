package com.glad.watchnext.app.view.home.tv.shows;

import com.glad.watchnext.app.usecase.tv.show.GetPresentationTvShowCategoriesUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.home.tv.shows.TvShowsContract.View;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.NetworkStateProvider.NetworkConnectionState;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import io.reactivex.disposables.Disposable;

/**
 * Created by Gautam Lad
 */
public final class TvShowsPresenter implements TvShowsContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(TvShowsPresenter.class);

    private TvShowsContract.View view;

    @NonNull private final GetPresentationTvShowCategoriesUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @Nullable private Disposable loadDisposable;
    @Nullable private Disposable networkDisposable;
    @IntRange (from = 0) private int loadedCount = 0;

    public TvShowsPresenter(
            @NonNull final GetPresentationTvShowCategoriesUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.schedulerProvider = schedulerProvider;
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ScreenContract.Presenter
    @Override
    public void bind(@NonNull final View view, @NonNull final Serializable... args) {
        this.view = view;
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        networkDisposable = networkStateProvider.asObservable()
                .filter(state -> state == NetworkConnectionState.CONNECTED)
                .subscribe(ignore -> loadData());

        loadData();
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        if (networkDisposable != null) {
            networkDisposable.dispose();
            networkDisposable = null;
        }
    }
    //endregion ScreenContract.Presenter

    //region TvShowsContract.Presenter
    @Override
    public void loadData() {
        log.d(TAG, "loadData() called");

        if (loadedCount > 0) {
            log.w(TAG, "loadData: Data already loaded :: loadedCount = [" + loadedCount + "]");
            return;
        }

        if (loadDisposable != null && !loadDisposable.isDisposed()) {
            log.w(TAG, "loadData: Load already in progress");
            return;
        }

        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        loadDisposable = useCase.execute(networkConnected)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(ignore -> {
                    loadedCount = 0;
                    view.setEmptyContentVisibility(false);
                    view.onLoadStarted();
                })
                .doOnNext(ignore -> ++loadedCount)
                .subscribe(view::onCollectionLoaded,
                        throwable -> {
                            view.setEmptyContentVisibility(true);
                            view.onError(throwable);
                        },
                        () -> {
                            view.setEmptyContentVisibility(loadedCount == 0);
                            view.onLoadComplete();
                        });
    }
    //endregion TvShowsContract.Presenter
}