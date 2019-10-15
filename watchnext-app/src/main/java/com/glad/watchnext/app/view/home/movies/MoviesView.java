package com.glad.watchnext.app.view.home.movies;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.home.collection.CollectionPageAdapter;
import com.glad.watchnext.app.view.home.movies.collection.MovieCollectionView;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;
import com.glad.watchnext.domain.service.LogService;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
public final class MoviesView extends FrameLayout implements MoviesContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(MoviesView.class);

    @Inject MoviesContract.Presenter presenter;
    @Inject LogService log;

    @BindView (R.id.tablayout) TabLayout tabLayout;
    @BindView (R.id.viewpager) ViewPager viewPager;
    @BindView (R.id.empty_content_view) View emptyContentView;

    @NonNull private final CollectionPageAdapter<MovieCollectionView> pageAdapter = new CollectionPageAdapter<>();

    //region Constructor
    public MoviesView(@NonNull final Context context) {
        this(context, null);
    }

    public MoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MoviesView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_home_movies, this);
        ButterKnife.bind(this);
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
        pageAdapter.clear();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region MoviesContract.View
    @Override
    public void onLoadStarted() {
        log.d(TAG, "onLoadStarted() called");
        pageAdapter.clear();
    }

    @Override
    public void onCollectionLoaded(@NonNull final CollectionPresentationModel collection) {
        log.d(TAG, "onCollectionLoaded() called with: collection = [" + collection + "]");
        final MovieCollectionView view = new MovieCollectionView(getContext());
        view.onCreated(collection);
        pageAdapter.add(collection, view);
    }

    @Override
    public void onLoadComplete() {
        log.d(TAG, "onLoadComplete() called");
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pageAdapter);
        if (pageAdapter.getCount() == 1) {
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEmptyContentVisibility(final boolean isVisible) {
        emptyContentView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion MoviesContract.View
}