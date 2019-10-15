package com.glad.watchnext.app.di.component;

import com.glad.watchnext.app.di.module.activity.ActivityModule;
import com.glad.watchnext.app.di.module.activity.NavigationModule;
import com.glad.watchnext.app.di.module.activity.ViewPresenterModule;
import com.glad.watchnext.app.di.scope.PerActivity;
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

import dagger.Subcomponent;

/**
 * Created by Gautam Lad
 */
@PerActivity
@Subcomponent (modules = {
        ActivityModule.class,
        ViewPresenterModule.class,
        NavigationModule.class
})
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(NetworkStateWidgetView view);

    void inject(SettingsScreen screen);
    void inject(SettingsView view);
    void inject(SettingsFragment fragment);

    void inject(SplashScreen screen);
    void inject(SplashView view);

    void inject(SearchScreen screen);
    void inject(SearchView view);
    void inject(MoviesSearchView view);
    void inject(TvShowsSearchView view);
    void inject(PeopleSearchView view);

    void inject(HomeScreen screen);
    void inject(HomeView view);

    void inject(MoviesView view);
    void inject(MovieCollectionView view);
    void inject(MovieDetailView view);
    void inject(SimilarMoviesView view);
    void inject(CastCreditsView view);

    void inject(TvShowsView view);
    void inject(TvShowCollectionView view);
    void inject(TvShowDetailView view);
    void inject(SimilarTvShowsView view);

    void inject(PeopleView view);
    void inject(PeopleCollectionView view);
    void inject(PersonDetailView view);
    void inject(MovieCreditsView view);
    void inject(TvShowCreditsView view);

    void inject(DetailScreen screen);
    void inject(DetailView view);

    void inject(ImageViewerScreen screen);
    void inject(ImageViewerView view);

    @Subcomponent.Builder
    interface Builder {
        Builder activityModule(ActivityModule module);
        Builder viewPresenterModule(ViewPresenterModule module);
        Builder navigationModule(NavigationModule module);
        ActivityComponent build();
    }
}
