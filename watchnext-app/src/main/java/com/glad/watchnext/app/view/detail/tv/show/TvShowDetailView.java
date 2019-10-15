package com.glad.watchnext.app.view.detail.tv.show;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.detail.credits.CastCreditsView;
import com.glad.watchnext.app.view.detail.similar.tv.shows.SimilarTvShowsView;
import com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel;
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
public final class TvShowDetailView extends FrameLayout implements TvShowDetailContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(TvShowDetailView.class);

    @Inject TvShowDetailContract.Presenter presenter;
    @Inject NavigationService navigationService;
    @Inject SchedulerProvider schedulerProvider;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;

    @BindView (R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView (R.id.img_poster) ImageView posterImageView;
    @BindView (R.id.img_backdrop) ImageView backdropImageView;
    @BindView (R.id.txt_name) TextView nameTextView;
    @BindView (R.id.txt_metadata1) TextView metadata1TextView;
    @BindView (R.id.txt_metadata2) TextView metadata2TextView;
    @BindView (R.id.txt_overview) TextView overviewTextView;
    @BindView (R.id.empty_content_view) View emptyContentView;

    @NonNull private static final String CAST_TYPE = "tv";
    @BindView (R.id.credits_container) ViewGroup creditsContainer;
    @NonNull private final CastCreditsView creditsView;

    @BindView (R.id.similar_tvshows_container) ViewGroup similarTvShowsContainer;
    @NonNull private final SimilarTvShowsView similarTvShowsView;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public TvShowDetailView(@NonNull final Context context) {
        this(context, null);
    }

    public TvShowDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvShowDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TvShowDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_detail_tvshow, this);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadData(true));
        creditsView = new CastCreditsView(context);
        similarTvShowsView = new SimilarTvShowsView(context);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        creditsView.onCreated(CAST_TYPE, args[0].toString());
        similarTvShowsView.onCreated(args[0].toString());
        presenter.bind(this, args);
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        presenter.subscribe();
        creditsView.onSubscribe();
        similarTvShowsView.onSubscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        creditsView.onUnsubscribe();
        similarTvShowsView.onUnsubscribe();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        creditsView.onDestroy();
        similarTvShowsView.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region TvShowDetailContract.View
    @Override
    public void onLoadStarted() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onLoadComplete(@NonNull final TvShowDetailedPresentationModel tvshow) {
        nameTextView.setText(tvshow.getName());
        metadata1TextView.setText(tvshow.getMetadataLine1());
        metadata2TextView.setText(tvshow.getMetadataLine2());
        overviewTextView.setText(tvshow.getOverview());

        final List<String> posterImageUrls = tvshow.getPosterImageUrls();
        if (!posterImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(posterImageView)
                            .url(posterImageUrls.get(0))
                            .placeholder(R.drawable.ic_tvshow_poster_placeholder)
                            .build())
                    .subscribe());
            posterImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(posterImageUrls));
        }

        final List<String> backdropImageUrls = tvshow.getBackdropImageUrls();
        if (!backdropImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(backdropImageView)
                            .url(backdropImageUrls.get(0))
                            .placeholder(R.drawable.ic_tvshow_backdrop_placeholder)
                            .build())
                    .subscribe());
            backdropImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(backdropImageUrls));
        }

        creditsContainer.removeAllViews();
        creditsContainer.addView(creditsView);

        similarTvShowsContainer.removeAllViews();
        similarTvShowsContainer.addView(similarTvShowsView);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setEmptyContentVisibility(final boolean isVisible) {
        emptyContentView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion TvShowDetailContract.View
}