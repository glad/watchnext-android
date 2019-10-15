package com.glad.watchnext.app.view.detail.tv.show;

import com.glad.watchnext.app.usecase.tv.show.GetPresentationTvShowDetailUseCase;
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
public final class TvShowDetailPresenter implements TvShowDetailContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(TvShowDetailPresenter.class);

    private TvShowDetailContract.View view;
    @NonNull private String tvShowId = "";

    @NonNull private final GetPresentationTvShowDetailUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean isLoaded = false;

    public TvShowDetailPresenter(
            @NonNull final GetPresentationTvShowDetailUseCase useCase,
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

    @Override
    public void bind(@NonNull final TvShowDetailContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        tvShowId = args[0].toString();
    }
    //region ViewPresenterContract.Presenter

    //region TvShowDetailContract.Presenter
    @Override
    public void loadData(final boolean ignoreCache) {
        log.d(TAG, "loadData() called with: ignoreCache = [" + ignoreCache + "]");
        disposables.add(useCase.execute(ignoreCache, tvShowId)
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
    //endregion TvShowDetailContract.Presenter
}