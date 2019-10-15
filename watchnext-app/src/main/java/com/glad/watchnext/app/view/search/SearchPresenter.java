package com.glad.watchnext.app.view.search;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.SearchHistoryService;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Gautam Lad
 */
public final class SearchPresenter implements SearchContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(SearchPresenter.class);

    @NonNull private final SearchHistoryService searchHistoryService;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    @Nullable private Disposable historyAddDisposable;

    private int loadedHistoryCount = 0;

    private SearchContract.View view;

    public SearchPresenter(
            @NonNull final SearchHistoryService searchHistoryService,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final LogService log) {
        this.searchHistoryService = searchHistoryService;
        this.schedulerProvider = schedulerProvider;
        this.log = log;
    }

    //region ScreenContract.Presenter
    @Override
    public void bind(@NonNull final SearchContract.View view, @NonNull final Serializable... args) {
        log.d(TAG, "bind() called with: view = [" + view + "], args = [" + args.length + "]");
        this.view = view;
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        updateSearchHistory();
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        if (historyAddDisposable != null) {
            historyAddDisposable.dispose();
            historyAddDisposable = null;
        }

        disposables.clear();
    }
    //endregion ScreenContract.Presenter

    //region SearchContract.Presenter
    @Override
    public void queryChanged(@NonNull final String query) {
        final boolean isEmpty = query.length() < 3;
        view.setHistoryVisibility(isEmpty);
        view.setResultsVisibility(!isEmpty);

        if (!isEmpty) {
            view.onPerformSearch(query);
            if (historyAddDisposable != null && !historyAddDisposable.isDisposed()) {
                historyAddDisposable.dispose();
            }
            historyAddDisposable = searchHistoryService.add(query)
                    .doOnComplete(this::updateSearchHistory)
                    .subscribe();
        }
    }

    @Override
    public void deleteHistory(@NonNull final String query) {
        disposables.add(searchHistoryService.delete(query)
                .subscribe());
    }
    //endregion SearchContract.Presenter

    private void updateSearchHistory() {
        disposables.add(searchHistoryService.getHistory()
                .filter(ignore -> loadedHistoryCount == 0 || ignore.size() != loadedHistoryCount)
                .doOnSuccess(list -> loadedHistoryCount = list.size())
                .subscribe(view::onSearchHistoryLoaded));
    }
}