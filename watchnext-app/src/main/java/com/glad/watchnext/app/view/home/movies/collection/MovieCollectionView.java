package com.glad.watchnext.app.view.home.movies.collection;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.home.collection.CollectionViewAdapter;
import com.glad.watchnext.app.view.home.collection.CollectionViewModel;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Gautam Lad
 */
public final class MovieCollectionView extends FrameLayout implements MovieCollectionContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(MovieCollectionView.class);

    @Inject MovieCollectionContract.Presenter presenter;
    @Inject ImageProvider imageProvider;
    @Inject SettingsProvider settingsProvider;
    @Inject LogService log;
    @Inject @Named ("isListView") ReplaySubject<Boolean> isListViewSubject;

    @BindView (R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView (R.id.recycler_view) RecyclerView recyclerView;
    @BindView (R.id.empty_content_view) View emptyContentView;

    @NonNull private final CollectionViewAdapter viewAdapter;

    //region Constructor
    public MovieCollectionView(@NonNull final Context context) {
        this(context, null);
    }

    public MovieCollectionView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieCollectionView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MovieCollectionView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_collection, this);
        ButterKnife.bind(this);
        viewAdapter = new CollectionViewAdapter(recyclerView, imageProvider, R.drawable.ic_movie_poster_placeholder);
        viewAdapter.getClickObservable()
                .subscribe(presenter::onClicked);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadData(true));
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);

        isListViewSubject.subscribe(isListView -> viewAdapter.updateView(getContext(), isListView ?
                CollectionViewAdapter.LIST_VIEWMODE :
                CollectionViewAdapter.GRID_VIEWMODE));
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        presenter.subscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region MovieCollectionContract.View
    @Override
    public void onLoadStarted() {
        log.d(TAG, "onLoadStarted() called");
        swipeRefreshLayout.setRefreshing(true);
        viewAdapter.clear();
    }

    @Override
    public void onLoaded(@NonNull final MovieSimplifiedPresentationModel movie) {
        log.d(TAG, "onLoaded() called with: movie = [" + movie + "]");
        viewAdapter.add(CollectionViewModel.newBuilder()
                .id(movie.getId())
                .title(movie.getTitle())
                .body(movie.getOverview())
                .imageUrl(movie.getPosterImageUrl())
                .build());
    }

    @Override
    public void onLoadComplete() {
        log.d(TAG, "onLoadComplete() called");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setEmptyContentVisibility(final boolean isVisible) {
        emptyContentView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion MovieCollectionContract.View
}