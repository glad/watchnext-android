package com.glad.watchnext.app.view.detail.person;

import com.glad.watchnext.app.usecase.person.GetPresentationPersonDetailUseCase;
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
public final class PersonDetailPresenter implements PersonDetailContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(PersonDetailPresenter.class);

    private PersonDetailContract.View view;
    @NonNull private String personId = "";

    @NonNull private final GetPresentationPersonDetailUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean isLoaded = false;

    public PersonDetailPresenter(
            @NonNull final GetPresentationPersonDetailUseCase useCase,
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
    public void bind(@NonNull final PersonDetailContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        personId = args[0].toString();
    }
    //region ViewPresenterContract.Presenter

    //region PersonDetailContract.Presenter
    @Override
    public void loadData(final boolean ignoreCache) {
        log.d(TAG, "loadData() called with: ignoreCache = [" + ignoreCache + "]");
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(ignoreCache && networkConnected, personId)
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
    //endregion PersonDetailContract.Presenter
}