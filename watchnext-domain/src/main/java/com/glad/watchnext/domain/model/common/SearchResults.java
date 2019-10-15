package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;

import java.util.List;

/**
 * Created by Gautam Lad
 */
public class SearchResults {
    private final List<MovieSimplified> movies;
    private final List<TvShowSimplified> tvshows;
    private final List<PersonSimplified> people;

    public static Builder newBuilder() {
        return new Builder();
    }

    private SearchResults(final Builder builder) {
        movies = builder.movies;
        tvshows = builder.tvshows;
        people = builder.people;
    }

    public List<MovieSimplified> getMovies() {
        return movies;
    }

    public List<TvShowSimplified> getTvShows() {
        return tvshows;
    }

    public List<PersonSimplified> getPeople() {
        return people;
    }

    public int getTotalResults() {
        return movies.size() + tvshows.size() + people.size();
    }

    //region Builder
    public static final class Builder {
        private List<MovieSimplified> movies;
        private List<TvShowSimplified> tvshows;
        private List<PersonSimplified> people;

        private Builder() {
        }

        public Builder movies(final List<MovieSimplified> val) {
            movies = val;
            return this;
        }

        public Builder tvshows(final List<TvShowSimplified> val) {
            tvshows = val;
            return this;
        }

        public Builder people(final List<PersonSimplified> val) {
            people = val;
            return this;
        }

        public SearchResults build() {
            return new SearchResults(this);
        }
    }
    //endregion Builder
}
