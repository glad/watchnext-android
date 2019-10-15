package com.glad.watchnext.app.di.module.app;

import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.provider.SearchDataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.common.GetCastCreditsUseCase;
import com.glad.watchnext.domain.usecase.movie.GetMovieCategoriesUseCase;
import com.glad.watchnext.domain.usecase.movie.GetMovieDetailUseCase;
import com.glad.watchnext.domain.usecase.movie.GetMoviesFromCategoryUseCase;
import com.glad.watchnext.domain.usecase.movie.GetSimilarMoviesUseCase;
import com.glad.watchnext.domain.usecase.people.GetMovieCreditsUseCase;
import com.glad.watchnext.domain.usecase.people.GetPeopleCategoriesUseCase;
import com.glad.watchnext.domain.usecase.people.GetPeopleFromCategoryUseCase;
import com.glad.watchnext.domain.usecase.people.GetPersonDetailUseCase;
import com.glad.watchnext.domain.usecase.people.GetTvShowCreditsUseCase;
import com.glad.watchnext.domain.usecase.search.GetMoviesSearchUseCase;
import com.glad.watchnext.domain.usecase.search.GetPeopleSearchUseCase;
import com.glad.watchnext.domain.usecase.search.GetTvShowsSearchUseCase;
import com.glad.watchnext.domain.usecase.tv.show.GetSimilarTvShowsUseCase;
import com.glad.watchnext.domain.usecase.tv.show.GetTvShowCategoriesUseCase;
import com.glad.watchnext.domain.usecase.tv.show.GetTvShowDetailUseCase;
import com.glad.watchnext.domain.usecase.tv.show.GetTvShowsFromCategoryUseCase;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class DomainUseCaseModule {
    //region Search
    @Provides
    GetMoviesSearchUseCase providesGetMoviesSearchUseCase(
            @NonNull final SearchDataProvider searchDataProvider,
            @NonNull final LogService log) {
        return new GetMoviesSearchUseCase(searchDataProvider, log);
    }

    @Provides
    GetTvShowsSearchUseCase providesGetTvShowsSearchUseCase(
            @NonNull final SearchDataProvider searchDataProvider,
            @NonNull final LogService log) {
        return new GetTvShowsSearchUseCase(searchDataProvider, log);
    }

    @Provides
    GetPeopleSearchUseCase providesGetPeopleSearchUseCase(
            @NonNull final SearchDataProvider searchDataProvider,
            @NonNull final LogService log) {
        return new GetPeopleSearchUseCase(searchDataProvider, log);
    }
    //endregion Search

    //region Movie
    @Provides
    GetMovieCategoriesUseCase providesGetMoviesCollectionsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetMovieCategoriesUseCase(dataProvider, log);
    }

    @Provides
    GetMoviesFromCategoryUseCase providesGetMoviesFromCollectionUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetMoviesFromCategoryUseCase(dataProvider, log);
    }

    @Provides
    GetMovieDetailUseCase providesGetMovieDetailUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetMovieDetailUseCase(dataProvider, log);
    }

    @Provides
    GetSimilarMoviesUseCase providesGetSimilarMoviesUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetSimilarMoviesUseCase(dataProvider, log);
    }

    @Provides
    GetCastCreditsUseCase providesGetMovieCreditsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetCastCreditsUseCase(dataProvider, log);
    }
    //endregion Movie

    //region TvShow
    @Provides
    GetTvShowCategoriesUseCase providesGetTvShowsCollectionsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetTvShowCategoriesUseCase(dataProvider, log);
    }

    @Provides
    GetTvShowsFromCategoryUseCase providesGetTvShowsFromCollectionUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetTvShowsFromCategoryUseCase(dataProvider, log);
    }

    @Provides
    GetTvShowDetailUseCase providesGetTvShowDetailUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetTvShowDetailUseCase(dataProvider, log);
    }

    @Provides
    GetSimilarTvShowsUseCase providesGetSimilarTvShowsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetSimilarTvShowsUseCase(dataProvider, log);
    }
    //endregion TvShow

    //region Person
    @Provides
    GetPeopleCategoriesUseCase providesGetPeopleCollectionsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetPeopleCategoriesUseCase(dataProvider, log);
    }

    @Provides
    GetPeopleFromCategoryUseCase providesGetPeopleFromCollectionUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetPeopleFromCategoryUseCase(dataProvider, log);
    }

    @Provides
    GetPersonDetailUseCase providesGetPersonDetailUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetPersonDetailUseCase(dataProvider, log);
    }

    @Provides
    GetMovieCreditsUseCase providesGetPersonMovieCreditsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetMovieCreditsUseCase(dataProvider, log);
    }

    @Provides
    GetTvShowCreditsUseCase providesGetTvShowCreditsUseCase(
            @NonNull final DataProvider dataProvider,
            @NonNull final LogService log) {
        return new GetTvShowCreditsUseCase(dataProvider, log);
    }
    //endregion Person
}