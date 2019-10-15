package com.glad.watchnext.data.provider.themoviedb;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.util.DomainModelMapper;
import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.exception.InvalidMovieCreditException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.exception.InvalidTvShowCreditException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.CastCredit;
import com.glad.watchnext.domain.model.common.Category;
import com.glad.watchnext.domain.model.common.MovieCredit;
import com.glad.watchnext.domain.model.common.TvShowCredit;
import com.glad.watchnext.domain.model.movie.MovieDetailed;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.model.person.PersonDetailed;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.model.tv.show.TvShowDetailed;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public final class TheMovieDbDataProvider implements DataProvider {
    private static final String TAG = TheMovieDbDataProvider.class.getSimpleName();

    private final DomainModelMapper modelMapper;
    private final LogService log;
    private final List<Category> movieCategories;
    private final List<Category> tvshowCategories;
    private final List<Category> peopleCategories;
    private final TheMovieDbDelegate delegate;

    public TheMovieDbDataProvider(final SettingsProvider settingsProvider, final LogService log) {
        this.log = log;

        delegate = new TheMovieDbDelegate(settingsProvider, log);
        modelMapper = new DomainModelMapper(settingsProvider);

        movieCategories = new ArrayList<>();
        tvshowCategories = new ArrayList<>();
        peopleCategories = new ArrayList<>();
        try {
            movieCategories.add(Category.newBuilder().id("popular").name("Popular").build());
            movieCategories.add(Category.newBuilder().id("top_rated").name("Top Rated").build());
            movieCategories.add(Category.newBuilder().id("upcoming").name("Upcoming").build());
            movieCategories.add(Category.newBuilder().id("now_playing").name("Now Playing").build());

            tvshowCategories.add(Category.newBuilder().id("popular").name("Popular").build());
            tvshowCategories.add(Category.newBuilder().id("top_rated").name("Top Rated").build());
            tvshowCategories.add(Category.newBuilder().id("on_the_air").name("On The Air").build());
            tvshowCategories.add(Category.newBuilder().id("airing_today").name("Airing Today").build());

            peopleCategories.add(Category.newBuilder().id("popular").name("Popular").build());
        } catch (final InvalidCategoryException e) {
            throw new RuntimeException(e);
        }
    }

    //region CastCredit
    @Override
    public Observable<CastCredit> fetchCastCredits(final String type, final String id) {
        log.d(TAG, "fetchCastCredits() called with: type = [" + type + "], id = [" + id + "]");
        return delegate.retrofit.fetchCredits(type, id)
                .doOnError(throwable -> log.e(TAG, "fetchCastCredits: " + throwable.getMessage(), throwable))
                .map(response -> response.cast)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(cast -> {
                            try {
                                return Observable.just(modelMapper.from(cast));
                            } catch (final InvalidCastException e) {
                                log.e(TAG, "fetchCastCredits: " + e.getMessage()
                                        + "\n" + cast.toString());
                                return Observable.error(e);
                            }
                        }));
    }
    //endregion CastCredit

    //region Movie
    @Override
    public Observable<Category> fetchMovieCategories() {
        return Observable.fromIterable(movieCategories);
    }

    @Override
    public Observable<MovieSimplified> fetchMoviesFromCategoryId(final String categoryId, final int pageIndex) {
        log.d(TAG, "fetchMoviesFromCategoryId() called with: categoryId = [" + categoryId + "], pageIndex = [" + pageIndex + "]");
        return delegate.retrofit.fetchMoviesFromCategoryId(categoryId, pageIndex)
                .doOnError(throwable -> log.e(TAG, "fetchMoviesFromCategoryId: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(movie -> {
                            try {
                                return Observable.just(modelMapper.from(movie));
                            } catch (final InvalidMovieException e) {
                                log.e(TAG, "fetchMoviesFromCategoryId: " + e.getMessage()
                                        + "\n" + movie.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public final Single<MovieDetailed> fetchMovieDetail(final String id) {
        log.d(TAG, "fetchMovieDetail() called with: id = [" + id + "]");
        return delegate.retrofit.fetchMovieDetail(id)
                .doOnError(throwable -> log.e(TAG, "fetchMovieDetail: " + throwable.getMessage(), throwable))
                .flatMap(movie -> {
                    try {
                        return Single.just(modelMapper.from(movie));
                    } catch (final InvalidMovieException e) {
                        log.e(TAG, "fetchMovieDetail: " + e.getMessage()
                                + "\n" + movie.toString());
                        return Single.error(e);
                    }
                });
    }

    @Override
    public Observable<MovieSimplified> fetchSimilarMovies(final String id) {
        log.d(TAG, "fetchSimilarMovies() called with: id = [" + id + "]");
        return delegate.retrofit.fetchSimilarMovies(id)
                .doOnError(throwable -> log.e(TAG, "fetchSimilarMovies: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(movie -> {
                            try {
                                return Observable.just(modelMapper.from(movie));
                            } catch (final InvalidMovieException e) {
                                log.e(TAG, "fetchSimilarMovies: " + e.getMessage()
                                        + "\n" + movie.toString());
                                return Observable.error(e);
                            }
                        }));
    }
    //endregion Movie

    //region TvShow
    @Override
    public Observable<Category> fetchTvShowCategories() {
        return Observable.fromIterable(tvshowCategories);
    }

    @Override
    public Observable<TvShowSimplified> fetchTvShowsFromCategoryId(final String categoryId, final int pageIndex) {
        log.d(TAG, "fetchTvShowsFromCategoryId() called with: categoryId = [" + categoryId + "], pageIndex = [" + pageIndex + "]");
        return delegate.retrofit.fetchTvShowsFromCategoryId(categoryId, pageIndex)
                .doOnError(throwable -> log.e(TAG, "fetchTvShowsFromCategoryId: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(tvshow -> {
                            try {
                                return Observable.just(modelMapper.from(tvshow));
                            } catch (final InvalidTvShowException e) {
                                log.e(TAG, "fetchTvShowsFromCategoryId: " + e.getMessage()
                                        + "\n" + tvshow.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public final Single<TvShowDetailed> fetchTvShowDetail(final String id) {
        log.d(TAG, "fetchTvShowDetail() called with: id = [" + id + "]");
        return delegate.retrofit.fetchTvShowDetail(id)
                .doOnError(throwable -> log.e(TAG, "fetchTvShowDetail: " + throwable.getMessage(), throwable))
                .flatMap(tvshow -> {
                    try {
                        return Single.just(modelMapper.from(tvshow));
                    } catch (final InvalidTvShowException e) {
                        log.e(TAG, "fetchTvShowDetail: " + e.getMessage()
                                + "\n" + tvshow.toString());
                        return Single.error(e);
                    }
                });
    }

    @Override
    public Observable<TvShowSimplified> fetchSimilarTvShows(final String id) {
        log.d(TAG, "fetchSimilarTvShows() called with: id = [" + id + "]");
        return delegate.retrofit.fetchSimilarTvShows(id)
                .doOnError(throwable -> log.e(TAG, "fetchSimilarTvShows: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(tvshow -> {
                            try {
                                return Observable.just(modelMapper.from(tvshow));
                            } catch (final InvalidTvShowException e) {
                                log.e(TAG, "fetchSimilarTvShows: " + e.getMessage()
                                        + "\n" + tvshow.toString());
                                return Observable.error(e);
                            }
                        }));
    }
    //endregion TvShow

    //region Person

    @Override
    public Observable<Category> fetchPeopleCategories() {
        return Observable.fromIterable(peopleCategories);
    }

    @Override
    public Observable<PersonSimplified> fetchPeopleFromCategoryId(final String categoryId, final int pageIndex) {
        log.d(TAG, "fetchTvShowsFromCategoryId() called with: categoryId = [" + categoryId + "], pageIndex = [" + pageIndex + "]");
        return delegate.retrofit.fetchPeopleFromCategoryId(categoryId, pageIndex)
                .doOnError(throwable -> log.e(TAG, "fetchPeopleFromCategoryId: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(person -> {
                            try {
                                return Observable.just(modelMapper.from(person));
                            } catch (final InvalidPersonException e) {
                                log.e(TAG, "fetchPersonsFromCollectionId: " + e.getMessage()
                                        + "\n" + person.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public final Single<PersonDetailed> fetchPersonDetail(final String id) {
        log.d(TAG, "fetchPersonDetail() called with: id = [" + id + "]");
        return delegate.retrofit.fetchPersonDetail(id)
                .doOnError(throwable -> log.e(TAG, "fetchPersonDetail: " + throwable.getMessage(), throwable))
                .flatMap(person -> {
                    try {
                        return Single.just(modelMapper.from(person));
                    } catch (final InvalidPersonException e) {
                        log.e(TAG, "fetchPersonDetail: " + e.getMessage()
                                + "\n" + person.toString());
                        return Single.error(e);
                    }
                });
    }

    @Override
    public final Observable<MovieCredit> fetchMovieCredits(final String id) {
        log.d(TAG, "fetchMovieCredits() called with: id = [" + id + "]");
        return delegate.retrofit.fetchPersonMovieCredits(id)
                .doOnError(throwable -> log.e(TAG, "fetchMovieCredits: " + throwable.getMessage(), throwable))
                .map(response -> response.cast)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(cast -> {
                            try {
                                return Observable.just(modelMapper.from(cast));
                            } catch (final InvalidMovieCreditException e) {
                                log.e(TAG, "fetchMovieCredits: " + e.getMessage()
                                        + "\n" + cast.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public final Observable<TvShowCredit> fetchTvShowCredits(final String id) {
        log.d(TAG, "fetchTvShowCredits() called with: id = [" + id + "]");
        return delegate.retrofit.fetchPersonTvShowCredits(id)
                .doOnError(throwable -> log.e(TAG, "fetchTvShowCredits: " + throwable.getMessage(), throwable))
                .map(response -> response.cast)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(cast -> {
                            try {
                                return Observable.just(modelMapper.from(cast));
                            } catch (final InvalidTvShowCreditException e) {
                                log.e(TAG, "fetchTvShowCredits: " + e.getMessage()
                                        + "\n" + cast.toString());
                                return Observable.error(e);
                            }
                        }));
    }
    //endregion Person
}