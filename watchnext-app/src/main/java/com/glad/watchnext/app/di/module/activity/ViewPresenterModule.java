package com.glad.watchnext.app.di.module.activity;

import com.glad.watchnext.app.usecase.movie.GetPresentationMovieCategoriesUseCase;
import com.glad.watchnext.app.usecase.movie.GetPresentationMovieDetailUseCase;
import com.glad.watchnext.app.usecase.movie.GetPresentationMoviesFromCategoryUseCase;
import com.glad.watchnext.app.usecase.movie.GetPresentationSimilarMoviesUseCase;
import com.glad.watchnext.app.usecase.person.GetPresentationMovieCreditsUseCase;
import com.glad.watchnext.app.usecase.person.GetPresentationPeopleCategoriesUseCase;
import com.glad.watchnext.app.usecase.person.GetPresentationPeopleFromCategoryUseCase;
import com.glad.watchnext.app.usecase.person.GetPresentationPersonDetailUseCase;
import com.glad.watchnext.app.usecase.person.GetPresentationTvShowCreditsUseCase;
import com.glad.watchnext.app.usecase.search.GetPresentationMoviesSearchUseCase;
import com.glad.watchnext.app.usecase.search.GetPresentationPeopleSearchUseCase;
import com.glad.watchnext.app.usecase.search.GetPresentationTvShowsSearchUseCase;
import com.glad.watchnext.app.usecase.tv.show.GetPresentationSimilarTvShowsUseCase;
import com.glad.watchnext.app.usecase.tv.show.GetPresentationTvShowCategoriesUseCase;
import com.glad.watchnext.app.usecase.tv.show.GetPresentationTvShowDetailUseCase;
import com.glad.watchnext.app.usecase.tv.show.GetPresentationTvShowsFromCategoryUseCase;
import com.glad.watchnext.app.view.detail.DetailContract;
import com.glad.watchnext.app.view.detail.DetailPresenter;
import com.glad.watchnext.app.view.detail.credits.CastCreditsContract;
import com.glad.watchnext.app.view.detail.credits.CastCreditsPresenter;
import com.glad.watchnext.app.view.detail.movie.MovieDetailContract;
import com.glad.watchnext.app.view.detail.movie.MovieDetailPresenter;
import com.glad.watchnext.app.view.detail.person.PersonDetailContract;
import com.glad.watchnext.app.view.detail.person.PersonDetailPresenter;
import com.glad.watchnext.app.view.detail.person.credits.movie.MovieCreditsContract;
import com.glad.watchnext.app.view.detail.person.credits.movie.MovieCreditsPresenter;
import com.glad.watchnext.app.view.detail.person.credits.tv.TvShowCreditsContract;
import com.glad.watchnext.app.view.detail.person.credits.tv.TvShowCreditsPresenter;
import com.glad.watchnext.app.view.detail.similar.movies.SimilarMoviesContract;
import com.glad.watchnext.app.view.detail.similar.movies.SimilarMoviesPresenter;
import com.glad.watchnext.app.view.detail.similar.tv.shows.SimilarTvShowsContract;
import com.glad.watchnext.app.view.detail.similar.tv.shows.SimilarTvShowsPresenter;
import com.glad.watchnext.app.view.detail.tv.show.TvShowDetailContract;
import com.glad.watchnext.app.view.detail.tv.show.TvShowDetailPresenter;
import com.glad.watchnext.app.view.home.HomeContract;
import com.glad.watchnext.app.view.home.HomePresenter;
import com.glad.watchnext.app.view.home.movies.MoviesContract;
import com.glad.watchnext.app.view.home.movies.MoviesPresenter;
import com.glad.watchnext.app.view.home.movies.collection.MovieCollectionContract;
import com.glad.watchnext.app.view.home.movies.collection.MovieCollectionPresenter;
import com.glad.watchnext.app.view.home.people.PeopleContract;
import com.glad.watchnext.app.view.home.people.PeoplePresenter;
import com.glad.watchnext.app.view.home.people.collection.PeopleCollectionContract;
import com.glad.watchnext.app.view.home.people.collection.PeopleCollectionPresenter;
import com.glad.watchnext.app.view.home.tv.shows.TvShowsContract;
import com.glad.watchnext.app.view.home.tv.shows.TvShowsPresenter;
import com.glad.watchnext.app.view.home.tv.shows.collection.TvShowCollectionContract;
import com.glad.watchnext.app.view.home.tv.shows.collection.TvShowCollectionPresenter;
import com.glad.watchnext.app.view.imageviewer.ImageViewerContract;
import com.glad.watchnext.app.view.imageviewer.ImageViewerPresenter;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.app.view.search.SearchContract;
import com.glad.watchnext.app.view.search.SearchPresenter;
import com.glad.watchnext.app.view.search.movies.MoviesSearchContract;
import com.glad.watchnext.app.view.search.movies.MoviesSearchPresenter;
import com.glad.watchnext.app.view.search.people.PeopleSearchContract;
import com.glad.watchnext.app.view.search.people.PeopleSearchPresenter;
import com.glad.watchnext.app.view.search.tv.shows.TvShowsSearchContract;
import com.glad.watchnext.app.view.search.tv.shows.TvShowsSearchPresenter;
import com.glad.watchnext.app.view.settings.SettingsContract;
import com.glad.watchnext.app.view.settings.SettingsPresenter;
import com.glad.watchnext.app.view.splash.SplashContract;
import com.glad.watchnext.app.view.splash.SplashPresenter;
import com.glad.watchnext.app.view.widget.networkstate.NetworkStateWidgetContract;
import com.glad.watchnext.app.view.widget.networkstate.NetworkStateWidgetPresenter;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.domain.service.SearchHistoryService;
import com.glad.watchnext.domain.usecase.common.GetCastCreditsUseCase;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class ViewPresenterModule {
    @Provides
    NetworkStateWidgetContract.Presenter providesNetworkStateWidgetPresenter(
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new NetworkStateWidgetPresenter(networkStateProvider, log);
    }

    //region Search
    @Provides
    SearchContract.Presenter providesSearchPresenter(
            @NonNull final SearchHistoryService searchHistoryService,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final LogService log) {
        return new SearchPresenter(searchHistoryService, schedulerProvider, log);
    }

    @Provides
    MoviesSearchContract.Presenter providesMovieSearchPresenter(
            @NonNull final GetPresentationMoviesSearchUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new MoviesSearchPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    TvShowsSearchContract.Presenter providesTvShowsSearchContract(
            @NonNull final GetPresentationTvShowsSearchUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new TvShowsSearchPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    PeopleSearchContract.Presenter providesPeopleSearchPresenter(
            @NonNull final GetPresentationPeopleSearchUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new PeopleSearchPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }
    //endregion Search

    @Provides
    HomeContract.Presenter providesHomePresenter(@NonNull final LogService log) {
        return new HomePresenter(log);
    }

    //region Movie
    @Provides
    MoviesContract.Presenter providesMoviesPresenter(
            @NonNull final GetPresentationMovieCategoriesUseCase useCase,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new MoviesPresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    MovieCollectionContract.Presenter providesMovieCollectionPresenter(
            @NonNull final GetPresentationMoviesFromCategoryUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new MovieCollectionPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    MovieDetailContract.Presenter providesMovieDetailPresenter(
            @NonNull final GetPresentationMovieDetailUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new MovieDetailPresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    SimilarMoviesContract.Presenter providesSimilarMoviesPresenter(
            @NonNull final GetPresentationSimilarMoviesUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new SimilarMoviesPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    CastCreditsContract.Presenter providesMovieCastPresenter(
            @NonNull final GetCastCreditsUseCase useCase,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new CastCreditsPresenter(useCase, modelMapper, schedulerProvider, navigationService, networkStateProvider, log);
    }
    //endregion Movie

    //region TV Shows
    @Provides
    TvShowsContract.Presenter providesTvShowsPresenter(
            @NonNull final GetPresentationTvShowCategoriesUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new TvShowsPresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    TvShowCollectionContract.Presenter providesTvShowCollectionPresenter(
            @NonNull final GetPresentationTvShowsFromCategoryUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new TvShowCollectionPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    TvShowDetailContract.Presenter providesTvShowDetailPresenter(
            @NonNull final GetPresentationTvShowDetailUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new TvShowDetailPresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    SimilarTvShowsContract.Presenter providesSimilarTvShowsPresenter(
            @NonNull final GetPresentationSimilarTvShowsUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new SimilarTvShowsPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }
    //endregion TV Shows

    //region People
    @Provides
    PeopleContract.Presenter providesPeoplePresenter(
            @NonNull final GetPresentationPeopleCategoriesUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new PeoplePresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    PeopleCollectionContract.Presenter providesPeopleCollectionPresenter(
            @NonNull final GetPresentationPeopleFromCategoryUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new PeopleCollectionPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    PersonDetailContract.Presenter providesPersonDetailPresenter(
            @NonNull final GetPresentationPersonDetailUseCase useCase,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new PersonDetailPresenter(useCase, schedulerProvider, networkStateProvider, log);
    }

    @Provides
    MovieCreditsContract.Presenter providesMovieCreditsPresenter(
            @NonNull final GetPresentationMovieCreditsUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new MovieCreditsPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }

    @Provides
    TvShowCreditsContract.Presenter providesTvShowCreditsPresenter(
            @NonNull final GetPresentationTvShowCreditsUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        return new TvShowCreditsPresenter(useCase, schedulerProvider, navigationService, networkStateProvider, log);
    }
    //endregion People

    @Provides
    SplashContract.Presenter providesSplashPresenter(@NonNull final LogService log) {
        return new SplashPresenter(log);
    }

    @Provides
    DetailContract.Presenter providesDetailPresenter(@NonNull final LogService log) {
        return new DetailPresenter(log);
    }

    @Provides
    ImageViewerContract.Presenter providesImageViewerPresenter(@NonNull final LogService log) {
        return new ImageViewerPresenter(log);
    }

    @Provides
    SettingsContract.Presenter providesSettingsPresenter(
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        return new SettingsPresenter(settingsProvider, log);
    }
}