package com.glad.watchnext.app.view.detail;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.detail.movie.MovieDetailView;
import com.glad.watchnext.app.view.detail.person.PersonDetailView;
import com.glad.watchnext.app.view.detail.tv.show.TvShowDetailView;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.util.ValueHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DetailView extends FrameLayout implements DetailContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(DetailView.class);

    @Inject DetailContract.Presenter presenter;
    @Inject LogService log;
    @Inject AppCompatActivity activity;

    @BindView (R.id.view_container) FrameLayout viewContainer;
    @BindView (R.id.toolbar) Toolbar toolbar;

    @Nullable private ViewPresenterContract.View currentView;

    //region Constructor
    public DetailView(@NonNull final Context context) {
        this(context, null);
    }

    public DetailView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DetailView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_detail, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = ValueHelper.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
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
        if (currentView != null) {
            currentView.onDestroy();
        }
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region DetailContract.View
    @Override
    public void showMovieDetail(@NonNull final String id) {
        log.d(TAG, "showMovieDetail() called with: id = [" + id + "]");
        updateView(id, new MovieDetailView(getContext()));
    }

    @Override
    public void showTvShowDetail(@NonNull final String id) {
        log.d(TAG, "showTvShowDetail() called with: id = [" + id + "]");
        updateView(id, new TvShowDetailView(getContext()));
    }

    @Override
    public void showPersonDetail(@NonNull final String id) {
        log.d(TAG, "showPersonDetail() called with: id = [" + id + "]");
        updateView(id, new PersonDetailView(getContext()));
    }

    private void updateView(@NonNull final String id, @NonNull final ViewPresenterContract.View view) {
        log.d(TAG, "updateView() called with: id = [" + id + "], view = [" + view + "]");
        if (currentView != null) {
            currentView.onDestroy();
        }
        currentView = view;
        viewContainer.removeAllViews();
        viewContainer.addView((View) currentView);
        currentView.onCreated(id);
        currentView.onSubscribe();
    }
    //endregion DetailContract.View
}