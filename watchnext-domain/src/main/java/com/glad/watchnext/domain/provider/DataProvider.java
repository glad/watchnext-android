package com.glad.watchnext.domain.provider;

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

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * An interface to allow data to be provided from a variety of sources (local cache, file, network, etc.)
 * <p>
 * Created by Gautam Lad
 */
public interface DataProvider {
    //region Movie
    /**
     * Fetches the content sections for Movies
     *
     * @return An {@link Observable} of {@link Category}
     */
    Observable<Category> fetchMovieCategories();

    /**
     * Fetches {@link MovieSimplified}s by {@link Category}
     *
     * @return An {@link Observable} of {@link MovieSimplified}
     */
    Observable<MovieSimplified> fetchMoviesFromCategoryId(final String categoryId, final int pageIndex);

    /**
     * Fetches {@link MovieDetailed} detail
     *
     * @return A {@link Single} of {@link MovieDetailed}
     */
    Single<MovieDetailed> fetchMovieDetail(final String id);

    /**
     * Fetches {@link MovieDetailed} detail
     *
     * @return A {@link Observable} of {@link MovieSimplified}
     */
    Observable<MovieSimplified> fetchSimilarMovies(final String id);
    //endregion Movie

    //region TvShow
    /**
     * Fetches the content sections for TvShows
     *
     * @return An {@link Observable} of {@link Category}
     */
    Observable<Category> fetchTvShowCategories();

    /**
     * Fetches {@link TvShowSimplified}s by {@link Category}
     *
     * @return An {@link Observable} of {@link TvShowSimplified}
     */
    Observable<TvShowSimplified> fetchTvShowsFromCategoryId(final String categoryId, final int pageIndex);

    /**
     * Fetches {@link TvShowDetailed} detail
     *
     * @return A {@link Single} of {@link TvShowDetailed}
     */
    Single<TvShowDetailed> fetchTvShowDetail(final String id);

    /**
     * Fetches {@link MovieDetailed} detail
     *
     * @return A {@link Observable} of {@link TvShowSimplified}
     */
    Observable<TvShowSimplified> fetchSimilarTvShows(final String id);
    //endregion TvShow

    //region Person
    /**
     * Fetches the content sections for Persons
     *
     * @return An {@link Observable} of {@link Category}
     */
    Observable<Category> fetchPeopleCategories();

    /**
     * Fetches {@link PersonSimplified}s by {@link Category}
     *
     * @return An {@link Observable} of {@link PersonSimplified}
     */
    Observable<PersonSimplified> fetchPeopleFromCategoryId(final String categoryId, final int pageIndex);

    /**
     * Fetches {@link PersonDetailed} detail
     *
     * @return A {@link Single} of {@link PersonDetailed}
     */
    Single<PersonDetailed> fetchPersonDetail(final String id);

    /**
     * Fetches credits info for a person
     *
     * @return A {@link Observable} of {@link MovieCredit}
     */
    Observable<MovieCredit> fetchMovieCredits(final String id);
    /**
     * Fetches credits info for a person
     *
     * @return A {@link Observable} of {@link TvShowCredit}
     */
    Observable<TvShowCredit> fetchTvShowCredits(final String id);
    //endregion Person

    //region Credits
    /**
     * Fetches credits for Movie and TV Shows
     *
     * @return A {@link Observable} of {@link CastCredit}
     */
    Observable<CastCredit> fetchCastCredits(final String type, final String id);
    //endregion Credits
}