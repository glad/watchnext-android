package com.glad.watchnext.app.view.home;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.home.movies.MoviesView;
import com.glad.watchnext.app.view.home.people.PeopleView;
import com.glad.watchnext.app.view.home.tv.shows.TvShowsView;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.domain.util.ValueHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

public final class HomeView extends FrameLayout implements HomeContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(HomeView.class);

    @Inject AppCompatActivity activity;
    @Inject HomeContract.Presenter presenter;
    @Inject NavigationService navigationService;
    @Inject LogService log;

    @BindView (R.id.bottom_navigation) BottomNavigationView bottomNavigation;
    @BindView (R.id.view_container) FrameLayout viewContainer;
    @BindView (R.id.toolbar) Toolbar toolbar;
    @BindView (R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView (R.id.nav_view) NavigationView navigationView;

    @Nullable private ViewPresenterContract.View currentView;

    //region Constructor
    public HomeView(@NonNull final Context context) {
        this(context, null);
    }

    public HomeView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HomeView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_home, this);
        ButterKnife.bind(this);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_movies:
                    presenter.onMoviesClicked();
                    return true;
                case R.id.nav_tvshows:
                    presenter.onTvShowsClicked();
                    return true;
                case R.id.nav_people:
                    presenter.onPeopleClicked();
                    return true;
            }
            return false;
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.drawer_menuitem_settings:
                    navigationService.navigateToConfig();
                    return true;
            }
            return false;
        });

        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = ValueHelper.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);
        bottomNavigation.setSelectedItemId(R.id.nav_movies); // Select default item triggering the content to load
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

    //region HomeContract.View
    @Override
    public void showMovies() {
        log.d(TAG, "showMovies() called");
        if (currentView instanceof MoviesView) {
            log.w(TAG, "showMovies: Already showing");
            return;
        }
        showCurrentView(new MoviesView(getContext()));
    }

    @Override
    public void showTvShows() {
        log.d(TAG, "showTvShows() called");
        if (currentView instanceof TvShowsView) {
            log.w(TAG, "showTvShows: Already showing");
            return;
        }
        showCurrentView(new TvShowsView(getContext()));
    }

    @Override
    public void showPeople() {
        log.d(TAG, "showPeople() called");
        if (currentView instanceof PeopleView) {
            log.w(TAG, "showPeople: Already showing");
            return;
        }

        showCurrentView(new PeopleView(getContext()));
    }

    private void showCurrentView(@NonNull final ViewPresenterContract.View view) {
        if (currentView != null) {
            currentView.onDestroy();
        }
        currentView = view;
        viewContainer.removeAllViews();
        viewContainer.addView((View) currentView);
        currentView.onCreated();
        currentView.onSubscribe();
    }
    //endregion HomeContract.View

    //region Screen Accessors
    void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    //endregion Screen Accessors
}