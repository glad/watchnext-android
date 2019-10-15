package com.glad.watchnext.app.di.module.app;

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
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
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

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public class PresentationUseCaseModule {
    //region Search
    @Provides
    GetPresentationMoviesSearchUseCase providesGetPresentationMovieSearchUseCase(
            @NonNull final GetMoviesSearchUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationMoviesSearchUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationTvShowsSearchUseCase providesGetPresentationTvShowsSearchUseCase(
            @NonNull final GetTvShowsSearchUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationTvShowsSearchUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationPeopleSearchUseCase providesGetPresentationPeopleSearchUseCase(
            @NonNull final GetPeopleSearchUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationPeopleSearchUseCase(useCase, cache, modelMapper, log);
    }
    //endregion Search

    //region Movie
    @Provides
    GetPresentationMovieCategoriesUseCase providesGetPresentationMovieCategoriesUseCase(
            @NonNull final GetMovieCategoriesUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationMovieCategoriesUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationMovieDetailUseCase providesGetPresentationMovieDetailUseCase(
            @NonNull final GetMovieDetailUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationMovieDetailUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationMoviesFromCategoryUseCase providesGetPresentationMoviesFromCategoryUseCase(
            @NonNull final GetMoviesFromCategoryUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationMoviesFromCategoryUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationSimilarMoviesUseCase providesGetPresentationSimilarMoviesUseCase(
            @NonNull final GetSimilarMoviesUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationSimilarMoviesUseCase(useCase, cache, modelMapper, log);
    }
    //endregion Movie

    //region TvShow
    @Provides
    GetPresentationTvShowCategoriesUseCase providesGetPresentationTvShowCategoriesUseCase(
            @NonNull final GetTvShowCategoriesUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationTvShowCategoriesUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationTvShowDetailUseCase providesGetPresentationTvShowDetailUseCase(
            @NonNull final GetTvShowDetailUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationTvShowDetailUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationTvShowsFromCategoryUseCase providesGetPresentationTvShowsFromCategoryUseCase(
            @NonNull final GetTvShowsFromCategoryUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationTvShowsFromCategoryUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationSimilarTvShowsUseCase providesGetPresentationSimilarTvShowsUseCase(
            @NonNull final GetSimilarTvShowsUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationSimilarTvShowsUseCase(useCase, cache, modelMapper, log);
    }
    //endregion TvShow

    //region Person
    @Provides
    GetPresentationPeopleCategoriesUseCase providesGetPresentationPeopleCategoriesUseCase(
            @NonNull final GetPeopleCategoriesUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationPeopleCategoriesUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationPersonDetailUseCase providesGetPresentationPersonDetailUseCase(
            @NonNull final GetPersonDetailUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationPersonDetailUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationPeopleFromCategoryUseCase providesGetPresentationPeopleFromCategoryUseCase(
            @NonNull final GetPeopleFromCategoryUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationPeopleFromCategoryUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationMovieCreditsUseCase providesGetPresentationMovieCreditsUseCase(
            @NonNull final GetMovieCreditsUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationMovieCreditsUseCase(useCase, cache, modelMapper, log);
    }

    @Provides
    GetPresentationTvShowCreditsUseCase providesGetPresentationTvShowCreditsUseCase(
            @NonNull final GetTvShowCreditsUseCase useCase,
            @NonNull @Named ("cache") final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        return new GetPresentationTvShowCreditsUseCase(useCase, cache, modelMapper, log);
    }
    //endregion Person
}