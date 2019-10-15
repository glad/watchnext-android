package com.glad.watchnext.app.view.detail.movie;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.detail.credits.CastCreditsView;
import com.glad.watchnext.app.view.detail.similar.movies.SimilarMoviesView;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class MovieDetailView extends FrameLayout implements MovieDetailContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(MovieDetailView.class);

    @Inject MovieDetailContract.Presenter presenter;
    @Inject NavigationService navigationService;
    @Inject SchedulerProvider schedulerProvider;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;

    @BindView (R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView (R.id.img_poster) ImageView posterImageView;
    @BindView (R.id.img_backdrop) ImageView backdropImageView;
    @BindView (R.id.txt_title) TextView titleTextView;
    @BindView (R.id.txt_metadata1) TextView metadata1TextView;
    @BindView (R.id.txt_metadata2) TextView metadata2TextView;
    @BindView (R.id.txt_overview) TextView overviewTextView;
    @BindView (R.id.empty_content_view) View emptyContentView;

    @BindView (R.id.credits_container) ViewGroup creditsContainer;
    @NonNull private final CastCreditsView creditsView;

    @NonNull private static final String CAST_TYPE = "movie";
    @BindView (R.id.similar_movies_container) ViewGroup similarMoviesContainer;
    @NonNull private final SimilarMoviesView similarMoviesView;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public MovieDetailView(@NonNull final Context context) {
        this(context, null);
    }

    public MovieDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MovieDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_detail_movie, this);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadData(true));
        creditsView = new CastCreditsView(context);
        similarMoviesView = new SimilarMoviesView(context);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        creditsView.onCreated(CAST_TYPE, args[0].toString());
        similarMoviesView.onCreated(args[0].toString());
        presenter.bind(this, args);
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        presenter.subscribe();
        creditsView.onSubscribe();
        similarMoviesView.onSubscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        creditsView.onUnsubscribe();
        similarMoviesView.onUnsubscribe();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        creditsView.onDestroy();
        similarMoviesView.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region MovieDetailContract.View
    @Override
    public void onLoadStarted() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onLoadComplete(@NonNull final MovieDetailedPresentationModel movie) {
        titleTextView.setText(movie.getTitle());
        metadata1TextView.setText(movie.getMetadataLine1());
        metadata2TextView.setText(movie.getMetadataLine2());
        overviewTextView.setText(movie.getOverview());

        final List<String> posterImageUrls = movie.getPosterImageUrls();
        if (!posterImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(posterImageView)
                            .url(posterImageUrls.get(0))
                            .placeholder(R.drawable.ic_movie_poster_placeholder)
                            .build())
                    .subscribe());
            posterImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(posterImageUrls));
        }

        final List<String> backdropImageUrls = movie.getBackdropImageUrls();
        if (!backdropImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(backdropImageView)
                            .url(backdropImageUrls.get(0))
                            .placeholder(R.drawable.ic_movie_backdrop_placeholder)
                            .build())
                    .subscribe());
            backdropImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(backdropImageUrls));
        }

        creditsContainer.removeAllViews();
        creditsContainer.addView(creditsView);

        similarMoviesContainer.removeAllViews();
        similarMoviesContainer.addView(similarMoviesView);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setEmptyContentVisibility(final boolean isVisible) {
        emptyContentView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion MovieDetailContract.View
}