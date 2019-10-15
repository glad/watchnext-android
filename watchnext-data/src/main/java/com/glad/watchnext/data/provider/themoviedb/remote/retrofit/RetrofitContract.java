package com.glad.watchnext.data.provider.themoviedb.remote.retrofit;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbMovieCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbResultSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbTvCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbMovieDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbMovieSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbPersonDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbPersonSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvShowDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvShowSimplified;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gautam Lad
 */
public interface RetrofitContract {
    //region Common
    @GET ("{type}/{id}/credits")
    Single<TmdbCredits> fetchCredits(
            @Path ("type") final String type,
            @Path ("id") final String id);
    //endregion Common

    //region Search
    @GET ("search/movie")
    Single<TmdbResultSimplified<TmdbMovieSimplified>> searchMovies(
            @Query ("query") final String query,
            @Query ("page") final int pageIndex);

    @GET ("search/tv")
    Single<TmdbResultSimplified<TmdbTvShowSimplified>> searchTvShows(
            @Query ("query") final String query,
            @Query ("page") final int pageIndex);

    @GET ("search/person")
    Single<TmdbResultSimplified<TmdbPersonSimplified>> searchPeople(
            @Query ("query") final String query,
            @Query ("page") final int pageIndex);
    //endregion Search

    //region Movie
    @GET ("movie/{categoryId}")
    Single<TmdbResultSimplified<TmdbMovieSimplified>> fetchMoviesFromCategoryId(
            @Path ("categoryId") final String categoryId,
            @Query ("page") final int pageIndex);

    @GET ("movie/{id}?append_to_response=release_dates,images")
    Single<TmdbMovieDetailed> fetchMovieDetail(
            @Path ("id") final String id);

    @GET ("movie/{id}/similar")
    Single<TmdbResultSimplified<TmdbMovieSimplified>> fetchSimilarMovies(
            @Path ("id") final String id);
    //endregion Movie

    //region Tv Shows
    @GET ("tv/{categoryId}")
    Single<TmdbResultSimplified<TmdbTvShowSimplified>> fetchTvShowsFromCategoryId(
            @Path ("categoryId") final String categoryId,
            @Query ("page") final int pageIndex);

    @GET ("tv/{id}?append_to_response=content_ratings,images")
    Single<TmdbTvShowDetailed> fetchTvShowDetail(
            @Path ("id") final String id);

    @GET ("tv/{id}/similar")
    Single<TmdbResultSimplified<TmdbTvShowSimplified>> fetchSimilarTvShows(
            @Path ("id") final String id);
    //endregion Tv Shows

    //region People
    @GET ("person/{categoryId}")
    Single<TmdbResultSimplified<TmdbPersonSimplified>> fetchPeopleFromCategoryId(
            @Path ("categoryId") final String categoryId,
            @Query ("page") final int pageIndex);

    @GET ("person/{id}?append_to_response=movie_credits,tv_credits,images")
    Single<TmdbPersonDetailed> fetchPersonDetail(
            @Path ("id") final String id);

    @GET ("person/{id}/movie_credits")
    Single<TmdbMovieCredits> fetchPersonMovieCredits(
            @Path ("id") final String id);

    @GET ("person/{id}/tv_credits")
    Single<TmdbTvCredits> fetchPersonTvShowCredits(
            @Path ("id") final String id);
    //endregion People
}
