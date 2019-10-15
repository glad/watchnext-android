package com.glad.watchnext.app.view.detail.similar.movies;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.detail.adapter.HorizontalImageViewAdapter;
import com.glad.watchnext.app.view.detail.adapter.HorizontalImageViewModel;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gautam Lad
 */
public final class SimilarMoviesView extends FrameLayout implements SimilarMoviesContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(SimilarMoviesView.class);

    @Inject SimilarMoviesContract.Presenter presenter;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;

    @BindView (R.id.recycler_view) RecyclerView recyclerView;

    @NonNull private final HorizontalImageViewAdapter viewAdapter;

    //region Constructor
    public SimilarMoviesView(@NonNull final Context context) {
        this(context, null);
    }

    public SimilarMoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimilarMoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimilarMoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_similar_movies, this);
        ButterKnife.bind(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        viewAdapter = new HorizontalImageViewAdapter(imageProvider, R.drawable.ic_movie_poster_placeholder);
        viewAdapter.getClickObservable()
                .subscribe(presenter::onClicked);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);
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

    //region SimilarMoviesContract.View
    @Override
    public void onLoadStarted() {
        viewAdapter.clear();
    }

    @Override
    public void onLoaded(@NonNull final MovieSimplifiedPresentationModel movie) {
        viewAdapter.add(HorizontalImageViewModel.newBuilder()
                .id(movie.getId())
                .imageUrl(movie.getPosterImageUrl())
                .build());
    }

    @Override
    public void onLoadComplete() {
        recyclerView.setAdapter(viewAdapter);
    }

    @Override
    public void setVisibility(final boolean isVisible) {
        setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion SimilarMoviesContract.View
}