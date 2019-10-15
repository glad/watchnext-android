package com.glad.watchnext.domain.provider;

import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;

import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public interface SearchDataProvider {
    /**
     * Searches {@link MovieSimplified}s by query string
     *
     * @return An {@link io.reactivex.Observable} of {@link MovieSimplified}
     */
    Observable<MovieSimplified> searchMovies(final String query, final int pageIndex);

    /**
     * Searches {@link TvShowSimplified}s by query string
     *
     * @return An {@link Observable} of {@link TvShowSimplified}
     */
    Observable<TvShowSimplified> searchTvShows(final String query, final int pageIndex);

    /**
     * Searches {@link PersonSimplified}s by query string
     *
     * @return An {@link Observable} of {@link PersonSimplified}
     */
    Observable<PersonSimplified> searchPeople(final String query, final int pageIndex);
}
