package com.glad.watchnext.app.view.detail.person;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.detail.person.credits.movie.MovieCreditsView;
import com.glad.watchnext.app.view.detail.person.credits.tv.TvShowCreditsView;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;
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
public final class PersonDetailView extends FrameLayout implements PersonDetailContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(PersonDetailView.class);

    @Inject PersonDetailContract.Presenter presenter;
    @Inject NavigationService navigationService;
    @Inject SchedulerProvider schedulerProvider;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;

    @BindView (R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView (R.id.img_profile) ImageView profileImageView;
    @BindView (R.id.img_backdrop) ImageView backdropImageView;
    @BindView (R.id.txt_name) TextView nameTextView;
    @BindView (R.id.txt_metadata1) TextView metadata1TextView;
    @BindView (R.id.txt_metadata2) TextView metadata2TextView;
    @BindView (R.id.txt_metadata3) TextView metadata3TextView;
    @BindView (R.id.txt_biography) TextView biographyTextView;
    @BindView (R.id.empty_content_view) View emptyContentView;

    @BindView (R.id.movie_credits_container) ViewGroup movieCreditsContainer;
    @NonNull private final MovieCreditsView movieCreditsView;

    @BindView (R.id.tvshow_credits_container) ViewGroup tvShowCreditsContainer;
    @NonNull private final TvShowCreditsView tvShowCreditsView;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public PersonDetailView(@NonNull final Context context) {
        this(context, null);
    }

    public PersonDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PersonDetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_detail_person, this);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadData(true));
        movieCreditsView = new MovieCreditsView(context);
        tvShowCreditsView = new TvShowCreditsView(context);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        movieCreditsView.onCreated(args[0].toString());
        tvShowCreditsView.onCreated(args[0].toString());
        presenter.bind(this, args);
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        movieCreditsView.onSubscribe();
        tvShowCreditsView.onSubscribe();
        presenter.subscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        movieCreditsView.onUnsubscribe();
        tvShowCreditsView.onUnsubscribe();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        movieCreditsView.onDestroy();
        tvShowCreditsView.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region PersonDetailContract.View
    @Override
    public void onLoadStarted() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onLoadComplete(@NonNull final PersonDetailedPresentationModel person) {
        nameTextView.setText(person.getName());
        metadata1TextView.setText(person.getMetadataLine1());
        metadata2TextView.setText(person.getMetadataLine2());
        metadata3TextView.setText(person.getMetadataLine3());
        metadata3TextView.setVisibility(person.getMetadataLine3().isEmpty() ? View.GONE : View.VISIBLE);
        biographyTextView.setText(person.getBiography());

        final List<String> profileImageUrls = person.getProfileImageUrls();
        if (!profileImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(profileImageView)
                            .url(profileImageUrls.get(0))
                            .placeholder(R.drawable.ic_person_profile_placeholder)
                            .build())
                    .subscribe());
            profileImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(profileImageUrls));
        }

        final List<String> backdropImageUrls = person.getBackdropImageUrls();
        if (!backdropImageUrls.isEmpty()) {
            disposables.add(imageProvider
                    .load(ImageProvider.Options.newBuilder()
                            .targetView(backdropImageView)
                            .url(backdropImageUrls.get(0))
                            .placeholder(R.drawable.ic_person_backdrop_placeholder)
                            .build())
                    .subscribe());
            backdropImageView.setOnClickListener(ignore -> navigationService.navigateToImageViewer(backdropImageUrls));
        }

        swipeRefreshLayout.setRefreshing(false);

        movieCreditsContainer.removeAllViews();
        movieCreditsContainer.addView(movieCreditsView);

        tvShowCreditsContainer.removeAllViews();
        tvShowCreditsContainer.addView(tvShowCreditsView);
    }

    @Override
    public void setEmptyContentVisibility(final boolean isVisible) {
        emptyContentView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion PersonDetailContract.View
}