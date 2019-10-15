package com.glad.watchnext.data.provider.themoviedb;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.util.DomainModelMapper;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;
import com.glad.watchnext.domain.provider.SearchDataProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;

import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class TheMovieDbSearchDataProvider implements SearchDataProvider {
    private static final String TAG = TheMovieDbSearchDataProvider.class.getSimpleName();

    private final TheMovieDbDelegate delegate;
    private final DomainModelMapper modelMapper;
    private final LogService log;

    public TheMovieDbSearchDataProvider(final SettingsProvider settingsProvider, final LogService log) {
        this.log = log;
        delegate = new TheMovieDbDelegate(settingsProvider, log);
        modelMapper = new DomainModelMapper(settingsProvider);
    }

    @Override
    public Observable<MovieSimplified> searchMovies(final String query, final int pageIndex) {
        return delegate.retrofit.searchMovies(query, pageIndex)
                .doOnError(throwable -> log.e(TAG, "searchMovies: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(movie -> {
                            try {
                                return Observable.just(modelMapper.from(movie));
                            } catch (final InvalidMovieException e) {
                                log.e(TAG, "searchMovies: " + e.getMessage()
                                        + "\n" + movie.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public Observable<TvShowSimplified> searchTvShows(final String query, final int pageIndex) {
        return delegate.retrofit.searchTvShows(query, pageIndex)
                .doOnError(throwable -> log.e(TAG, "searchTvShows: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(tvshow -> {
                            try {
                                return Observable.just(modelMapper.from(tvshow));
                            } catch (final InvalidTvShowException e) {
                                log.e(TAG, "searchTvShows: " + e.getMessage()
                                        + "\n" + tvshow.toString());
                                return Observable.error(e);
                            }
                        }));
    }

    @Override
    public Observable<PersonSimplified> searchPeople(final String query, final int pageIndex) {
        log.d(TAG, "searchPeople() called with: query = [" + query + "], pageIndex = [" + pageIndex + "]");
        return delegate.retrofit.searchPeople(query, pageIndex)
                .doOnError(throwable -> log.e(TAG, "searchPeople: " + throwable.getMessage(), throwable))
                .map(response -> response.results)
                .flatMapObservable(list -> Observable.fromIterable(list)
                        .flatMap(person -> {
                            try {
                                return Observable.just(modelMapper.from(person));
                            } catch (final InvalidPersonException e) {
                                log.e(TAG, "searchPeople: " + e.getMessage()
                                        + "\n" + person.toString());
                                return Observable.error(e);
                            }
                        }));
    }
}