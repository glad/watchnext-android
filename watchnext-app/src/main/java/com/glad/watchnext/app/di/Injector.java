package com.glad.watchnext.app.di;

import com.glad.watchnext.app.di.component.ActivityComponent;
import com.glad.watchnext.app.di.component.AppComponent;
import com.glad.watchnext.app.di.component.DaggerAppComponent;
import com.glad.watchnext.app.di.module.activity.ActivityModule;
import com.glad.watchnext.app.di.module.activity.NavigationModule;
import com.glad.watchnext.app.di.module.activity.ViewPresenterModule;
import com.glad.watchnext.app.di.module.app.AppModule;
import com.glad.watchnext.app.di.module.app.AppServiceModule;
import com.glad.watchnext.app.di.module.app.DomainUseCaseModule;
import com.glad.watchnext.app.di.module.app.ModelMapperModule;
import com.glad.watchnext.app.di.module.app.PresentationUseCaseModule;
import com.glad.watchnext.app.di.module.app.ProviderModule;
import com.glad.watchnext.app.view.MainActivity;
import com.glad.watchnext.app.view.detail.DetailScreen;
import com.glad.watchnext.app.view.detail.DetailView;
import com.glad.watchnext.app.view.detail.credits.CastCreditsView;
import com.glad.watchnext.app.view.detail.movie.MovieDetailView;
import com.glad.watchnext.app.view.detail.person.PersonDetailView;
import com.glad.watchnext.app.view.detail.person.credits.movie.MovieCreditsView;
import com.glad.watchnext.app.view.detail.person.credits.tv.TvShowCreditsView;
import com.glad.watchnext.app.view.detail.similar.movies.SimilarMoviesView;
import com.glad.watchnext.app.view.detail.similar.tv.shows.SimilarTvShowsView;
import com.glad.watchnext.app.view.detail.tv.show.TvShowDetailView;
import com.glad.watchnext.app.view.home.HomeScreen;
import com.glad.watchnext.app.view.home.HomeView;
import com.glad.watchnext.app.view.home.movies.MoviesView;
import com.glad.watchnext.app.view.home.movies.collection.MovieCollectionView;
import com.glad.watchnext.app.view.home.people.PeopleView;
import com.glad.watchnext.app.view.home.people.collection.PeopleCollectionView;
import com.glad.watchnext.app.view.home.tv.shows.TvShowsView;
import com.glad.watchnext.app.view.home.tv.shows.collection.TvShowCollectionView;
import com.glad.watchnext.app.view.imageviewer.ImageViewerScreen;
import com.glad.watchnext.app.view.imageviewer.ImageViewerView;
import com.glad.watchnext.app.view.search.SearchScreen;
import com.glad.watchnext.app.view.search.SearchView;
import com.glad.watchnext.app.view.search.movies.MoviesSearchView;
import com.glad.watchnext.app.view.search.people.PeopleSearchView;
import com.glad.watchnext.app.view.search.tv.shows.TvShowsSearchView;
import com.glad.watchnext.app.view.settings.SettingsFragment;
import com.glad.watchnext.app.view.settings.SettingsScreen;
import com.glad.watchnext.app.view.settings.SettingsView;
import com.glad.watchnext.app.view.splash.SplashScreen;
import com.glad.watchnext.app.view.splash.SplashView;
import com.glad.watchnext.app.view.widget.networkstate.NetworkStateWidgetView;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public enum Injector {
    INSTANCE;

    private AppComponent appComponent;
    private ActivityComponent activityComponent;

    public void init(@NonNull final Application application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .serviceModule(new AppServiceModule(application))
                .mapperModule(new ModelMapperModule())
                .providerModule(new ProviderModule())
                .useCaseModule(new DomainUseCaseModule())
                .presentationUseCaseModule(new PresentationUseCaseModule())
                .build();
        appComponent.inject(application);
    }

    public void inject(@NonNull final MainActivity activity) {
        activityComponent = appComponent.activityComponentBuilder()
                .activityModule(new ActivityModule(activity))
                .viewPresenterModule(new ViewPresenterModule())
                .navigationModule(new NavigationModule())
                .build();
        activityComponent.inject(activity);
    }

    //region Splash
    public void inject(@NonNull final SplashScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final SplashView view) {
        activityComponent.inject(view);
    }
    //endregion Splash

    //region Search
    public void inject(@NonNull final SearchScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final SearchView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final MoviesSearchView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final TvShowsSearchView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final PeopleSearchView view) {
        activityComponent.inject(view);
    }

    //endregion Search

    //region Home
    public void inject(@NonNull final HomeScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final HomeView view) {
        activityComponent.inject(view);
    }
    //endregion Home

    //region Detail
    public void inject(@NonNull final DetailScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final DetailView view) {
        activityComponent.inject(view);
    }
    //endregion Detail

    //region Widgets
    public void inject(@NonNull final NetworkStateWidgetView view) {
        activityComponent.inject(view);
    }
    //endregion Widgets

    //region Movies
    public void inject(@NonNull final MoviesView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final MovieCollectionView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final MovieDetailView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final SimilarMoviesView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final CastCreditsView view) {
        activityComponent.inject(view);
    }
    //endregion Movies

    //region Tv Shows
    public void inject(@NonNull final TvShowsView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final TvShowCollectionView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final TvShowDetailView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final SimilarTvShowsView view) {
        activityComponent.inject(view);
    }
    //endregion Tv Shows

    //region People
    public void inject(@NonNull final PeopleView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final PeopleCollectionView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final PersonDetailView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final MovieCreditsView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final TvShowCreditsView view) {
        activityComponent.inject(view);
    }
    //endregion People

    //region Image Viewer
    public void inject(@NonNull final ImageViewerScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final ImageViewerView view) {
        activityComponent.inject(view);
    }
    //endregion Image Viewer

    //region Settings
    public void inject(@NonNull final SettingsScreen screen) {
        activityComponent.inject(screen);
    }

    public void inject(@NonNull final SettingsView view) {
        activityComponent.inject(view);
    }

    public void inject(@NonNull final SettingsFragment fragment) {
        activityComponent.inject(fragment);
    }
    //endregion Settings
}