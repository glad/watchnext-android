package com.glad.watchnext.app.view.search;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.search.adapter.HistoryViewAdapter;
import com.glad.watchnext.app.view.search.adapter.SearchTypePagerAdapter;
import com.glad.watchnext.app.view.search.movies.MoviesSearchView;
import com.glad.watchnext.app.view.search.people.PeopleSearchView;
import com.glad.watchnext.app.view.search.tv.shows.TvShowsSearchView;
import com.glad.watchnext.app.widget.SearchBarWidget;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.util.ValueHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public final class SearchView extends FrameLayout implements SearchContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(SearchView.class);

    @Inject AppCompatActivity activity;
    @Inject SearchContract.Presenter presenter;
    @Inject LogService log;
    @Inject SchedulerProvider schedulerProvider;

    @BindView (R.id.toolbar) Toolbar toolbar;

    @BindView (R.id.searchView) SearchBarWidget searchBarWidget;
    @BindView (R.id.tablayout) TabLayout tabLayout;
    @BindView (R.id.viewpager) ViewPager viewPager;
    @BindView (R.id.empty_content_view) View emptyContentView;
    @BindView (R.id.history_recycler_view) RecyclerView historyRecyclerView;

    @NonNull private final HistoryViewAdapter historyViewAdapter = new HistoryViewAdapter();
    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public SearchView(@NonNull final Context context) {
        this(context, null);
    }

    public SearchView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SearchView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_search, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = ValueHelper.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(historyViewAdapter);
        historyViewAdapter.getClickObservable()
                .subscribe(searchBarWidget::setText);
        historyViewAdapter.getDeleteObservable()
                .subscribe(presenter::deleteHistory);

        tabLayout.setupWithViewPager(viewPager);
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

        disposables.add(searchBarWidget
                .getQueryChangeObservable()
                .debounce(500L, TimeUnit.MILLISECONDS, schedulerProvider.ui())
                .map(String::trim)
                .distinctUntilChanged()
                .subscribe(presenter::queryChanged));

        presenter.subscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        disposables.clear();
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

    //region SearchContract.View
    @Override
    public void onSearchHistoryLoaded(@NonNull final List<String> items) {
        historyViewAdapter.clear();
        historyViewAdapter.addAll(items);
    }

    @Override
    public void onPerformSearch(final String query) {
        final Context context = getContext();

        final SearchTypePagerAdapter<View> pagerAdapter = new SearchTypePagerAdapter<>();

        final MoviesSearchView movies = new MoviesSearchView(context);
        movies.onCreated(query);
        pagerAdapter.add(getResources().getString(R.string.movies), movies);

        final TvShowsSearchView tvshows = new TvShowsSearchView(context);
        tvshows.onCreated(query);
        pagerAdapter.add(getResources().getString(R.string.tvshows), tvshows);

        final PeopleSearchView people = new PeopleSearchView(context);
        people.onCreated(query);
        pagerAdapter.add(getResources().getString(R.string.people), people);

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void setResultsVisibility(final boolean isVisible) {
        viewPager.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setHistoryVisibility(final boolean isVisible) {
        historyRecyclerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    //endregion SearchContract.View
}