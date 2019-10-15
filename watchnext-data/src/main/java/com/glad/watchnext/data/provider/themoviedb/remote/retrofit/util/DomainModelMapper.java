package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.util;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbCastCredit;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbEnum;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbGenre;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbImage;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbMovieCredit;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbMovieCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbTvCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbTvShowCredit;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbMovieDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbMovieSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbReleaseDate;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbReleaseDateResult;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbKnownFor;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbPersonDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbPersonSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvRating;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvShowDetailed;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvShowSimplified;
import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.exception.InvalidGenreException;
import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.exception.InvalidMovieCreditException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.exception.InvalidTvShowCreditException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.CastCredit;
import com.glad.watchnext.domain.model.common.Genre;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.model.common.MovieCredit;
import com.glad.watchnext.domain.model.common.TvShowCredit;
import com.glad.watchnext.domain.model.movie.MovieDetailed;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.model.person.PersonDetailed;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.model.tv.show.TvShowDetailed;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.util.ValueHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A collection of methods responsible for mapping Retrofit Tmdb models to Domain models.
 * <p>
 * Created by Gautam Lad
 */
@SuppressWarnings ("WeakerAccess")
public final class DomainModelMapper {
    private final SettingsProvider settingsProvider;

    public DomainModelMapper(final SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    //region Genre

    /**
     * @return A {@link Genre} mapped from {@link TmdbGenre}
     *
     * @throws InvalidGenreException thrown on error
     */
    public Genre from(final TmdbGenre val) throws InvalidGenreException {
        return Genre.newBuilder()
                .id(String.valueOf(val.id))
                .name(val.name)
                .build();
    }

    /**
     * @return A {@link List} of {@link Genre}s mapped from a {@link List} of  {@link TmdbGenre}s
     *
     * @throws InvalidGenreException thrown on error
     */
    public List<Genre> fromGenres(final List<TmdbGenre> list) throws InvalidGenreException {
        final List<Genre> result = new ArrayList<>();
        for (final TmdbGenre genre : list) {
            result.add(from(genre));
        }
        return result;
    }
    //endregion Genre

    //region Movie

    /**
     * @return A {@link MovieSimplified} mapped from {@link TmdbMovieSimplified}
     *
     * @throws InvalidMovieException thrown on error
     */
    public MovieSimplified from(final TmdbMovieSimplified val) throws InvalidMovieException {
        return MovieSimplified.newBuilder()
                .id(String.valueOf(val.id))
                .title(val.title)
                .overview(val.overview)
                .posterImage(from(val.poster_path, settingsProvider.getPosterImageWidth()))
                .backdropImage(from(val.backdrop_path, settingsProvider.getBackdropImageWidth()))
                .build();
    }

    /**
     * @return A {@link MovieDetailed} mapped from {@link TmdbMovieDetailed}
     *
     * @throws InvalidMovieException thrown on error
     */
    public MovieDetailed from(final TmdbMovieDetailed val) throws InvalidMovieException {
        try {
            // Get country code based on production company - or default to locale
            final String countryCode;
            if (val.production_countries != null && !val.production_countries.isEmpty()) {
                countryCode = ValueHelper.nullToDefault(ValueHelper.emptyToNull(val.production_countries.get(0).iso_3166_1),
                        settingsProvider.getCountryCode());
            } else {
                countryCode = settingsProvider.getCountryCode();
            }

            // Find rating based on release information for current language/country locale
            String certification = null;
            Date releaseDate = null;
            if (val.release_dates != null) {
                final List<TmdbReleaseDateResult> outerList = val.release_dates.results;

                for (final TmdbReleaseDateResult outer : outerList) {
                    if (countryCode.equals(ValueHelper.nullToEmpty(outer.iso_3166_1))) {
                        final List<TmdbReleaseDate> innerList = outer.release_dates;

                        for (final TmdbReleaseDate inner : innerList) {
                            final String cleanCertification = ValueHelper.emptyToNull(inner.certification);
                            if (cleanCertification != null) {
                                certification = cleanCertification;
                                releaseDate = inner.release_date;
                            }

                            // If we have a match for theatrical release, return built model
                            if (inner.type == TmdbEnum.ReleaseType.THEATRICAL && certification != null) {
                                break;
                            }
                        }

                        break;
                    }
                }
            }

            // Get the list of poster and backdrop images
            final List<Image> posterImages;
            final List<Image> backdropImages;
            if (val.images != null) {
                posterImages = from(val.images.posters, settingsProvider.getPosterImageWidth());
                final Image posterImage = from(val.poster_path, settingsProvider.getPosterImageWidth());
                if (posterImage != null) {
                    posterImages.add(0, posterImage);
                }

                backdropImages = from(val.images.backdrops, settingsProvider.getBackdropImageWidth());
                final Image backdropImage = from(val.backdrop_path, settingsProvider.getBackdropImageWidth());
                if (backdropImage != null) {
                    backdropImages.add(0, backdropImage);
                }
            } else {
                posterImages = null;
                backdropImages = null;
            }

            return MovieDetailed.newBuilder()
                    .id(String.valueOf(val.id))
                    .title(val.title)
                    .overview(val.overview)
                    .posterImages(posterImages)
                    .backdropImages(backdropImages)
                    .releaseDate(releaseDate)
                    .certification(certification)
                    .countryCode(countryCode)
                    .runtimeMinutes(val.runtime)
                    .genres(fromGenres(val.genres))
                    .build();
        } catch (final InvalidMovieException | InvalidGenreException e) {
            throw new InvalidMovieException(e);
        }
    }
    //endregion Movie

    //region TvShow

    /**
     * @return A {@link TvShowSimplified} mapped from {@link TmdbTvShowSimplified}
     *
     * @throws InvalidTvShowException thrown on error
     */
    public TvShowSimplified from(final TmdbTvShowSimplified val) throws InvalidTvShowException {
        return TvShowSimplified.newBuilder()
                .id(String.valueOf(val.id))
                .name(val.name)
                .overview(val.overview)
                .posterImage(from(val.poster_path, settingsProvider.getPosterImageWidth()))
                .backdropImage(from(val.backdrop_path, settingsProvider.getBackdropImageWidth()))
                .build();
    }

    /**
     * @return A {@link TvShowDetailed} mapped from {@link TmdbTvShowDetailed}
     *
     * @throws InvalidTvShowException thrown on error
     */
    public TvShowDetailed from(final TmdbTvShowDetailed val) throws InvalidTvShowException {
        try {
            // Get country code based on production company - or default to locale
            final String countryCode;
            if (val.origin_country != null && !val.origin_country.isEmpty()) {
                countryCode = ValueHelper.nullToDefault(ValueHelper.emptyToNull(val.origin_country.get(0)),
                        settingsProvider.getCountryCode());
            } else {
                countryCode = settingsProvider.getCountryCode();
            }

            // Find rating based on release information for current language/country locale
            String rating = null;
            if (val.content_ratings != null) {
                final List<TmdbTvRating> innerList = val.content_ratings.results;
                for (final TmdbTvRating inner : innerList) {
                    if (countryCode.matches(inner.iso_3166_1)) {
                        rating = ValueHelper.emptyToNull(inner.rating);
                        break;
                    }
                }
            }

            // Get the list of poster and backdrop images
            final List<Image> posterImages;
            final List<Image> backdropImages;
            if (val.images != null) {
                posterImages = from(val.images.posters, settingsProvider.getPosterImageWidth());
                final Image posterImage = from(val.poster_path, settingsProvider.getPosterImageWidth());
                if (posterImage != null) {
                    posterImages.add(0, posterImage);
                }

                backdropImages = from(val.images.backdrops, settingsProvider.getBackdropImageWidth());
                final Image backdropImage = from(val.backdrop_path, settingsProvider.getBackdropImageWidth());
                if (backdropImage != null) {
                    backdropImages.add(0, backdropImage);
                }
            } else {
                posterImages = null;
                backdropImages = null;
            }

            // Get country code based on production company - or default to locale
            return TvShowDetailed.newBuilder()
                    .id(String.valueOf(val.id))
                    .name(val.name)
                    .overview(val.overview)
                    .posterImages(posterImages)
                    .backdropImages(backdropImages)
                    .network(val.networks != null ? val.networks.get(0).name : null)
                    .rating(rating)
                    .countryCode(countryCode)
                    .runtimeMinutes(val.runtime != null ? val.runtime.get(0) : 0L)
                    .genres(fromGenres(val.genres))
                    .build();
        } catch (final InvalidTvShowException | InvalidGenreException e) {
            throw new InvalidTvShowException(e);
        }
    }
    //endregion TvShow

    //region Person

    /**
     * @return A {@link PersonSimplified} mapped from {@link TmdbPersonSimplified}
     *
     * @throws InvalidPersonException thrown on error
     */
    public PersonSimplified from(final TmdbPersonSimplified val) throws InvalidPersonException {
        final List<String> knownFor = new ArrayList<>();
        if (val.known_for != null) {
            for (final TmdbKnownFor k : val.known_for) {
                if (k.value instanceof TmdbMovieSimplified) {
                    knownFor.add(((TmdbMovieSimplified) k.value).title);
                } else if (k.value instanceof TmdbTvShowSimplified) {
                    knownFor.add(((TmdbTvShowSimplified) k.value).name);
                }
            }
        }
        return PersonSimplified.newBuilder()
                .id(String.valueOf(val.id))
                .name(val.name)
                .knownFor(knownFor)
                .profileImage(from(val.profile_path, settingsProvider.getProfileImageWidth()))
                .build();
    }

    /**
     * @return A {@link PersonDetailed} mapped from {@link TmdbPersonDetailed}
     *
     * @throws InvalidPersonException thrown on error
     */
    public PersonDetailed from(final TmdbPersonDetailed val) throws InvalidPersonException {
        try {
            // Get the list of poster and backdrop image urls
            final List<Image> profileImages;
            if (val.images != null) {
                profileImages = from(val.images.profiles, settingsProvider.getProfileImageWidth());
                final Image profileImage = from(val.profile_path, settingsProvider.getProfileImageWidth());
                if (profileImage != null) {
                    profileImages.add(0, profileImage);
                }
            } else {
                profileImages = null;
            }

            return PersonDetailed.newBuilder()
                    .id(String.valueOf(val.id))
                    .name(val.name)
                    .biography(val.biography)
                    .birthDate(val.birthday)
                    .deathDate(val.deathday)
                    .birthPlace(val.place_of_birth)
                    .profileImages(profileImages)
                    .movieCasts(from(val.movie_credits))
                    .tvShowCasts(from(val.tv_credits))
                    .build();
        } catch (final InvalidPersonException | InvalidMovieCreditException | InvalidTvShowCreditException e) {
            throw new InvalidPersonException(e);
        }
    }
    //endregion Person

    //region Images
    public String imageUrlFrom(final String path, final String width) {
        try {
            return settingsProvider.getImagesBaseUrl()
                    + "/" + ValueHelper.requireValue(width, "Width cannot be null or empty")
                    + "/" + ValueHelper.requireValue(path, "Path cannot be null or empty");
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    public Image from(final String path, final String width) {
        try {
            return Image.newBuilder()
                    .url(imageUrlFrom(path, width))
                    .build();
        } catch (final InvalidImageException e) {
            return null;
        }
    }

    /**
     * @return A {@link Image} mapped from a list of {@link TmdbImage} or null on error
     */
    public Image from(final TmdbImage image, final String width) {
        try {
            return Image.newBuilder()
                    .width(image.width)
                    .height(image.height)
                    .url(imageUrlFrom(image.file_path, width))
                    .build();
        } catch (final NullPointerException | InvalidImageException e) {
            return null;
        }
    }

    /**
     * @return A {@link List} of {@link Image} mapped from a list of {@link TmdbImage} or null if empty or not available
     */
    public List<Image> from(final List<TmdbImage> images, final String width) {
        final List<Image> result = new ArrayList<>();

        if (images != null) {
            for (final TmdbImage i : images) {
                final Image image = from(i, width);
                if (image != null) {
                    result.add(image);
                }
            }
        }

        return result;
    }
    //endregion Images

    //region Credits

    /**
     * @return A {@link CastCredit}s mapped from a {@link TmdbCastCredit}
     *
     * @throws InvalidCastException thrown on error
     */
    public CastCredit from(final TmdbCastCredit credit) throws InvalidCastException {
        return CastCredit.newBuilder()
                .id(String.valueOf(credit.id))
                .character(credit.character)
                .name(credit.name)
                .profileImage(from(credit.profile_path, settingsProvider.getProfileImageWidth()))
                .build();
    }

    /**
     * @return A {@link MovieCredit}s mapped from a {@link TmdbMovieCredit}
     *
     * @throws InvalidMovieCreditException thrown on error
     */
    public MovieCredit from(final TmdbMovieCredit credit) throws InvalidMovieCreditException {
        return MovieCredit.newBuilder()
                .id(String.valueOf(credit.id))
                .character(credit.character)
                .title(credit.title)
                .overview(credit.overview)
                .posterImage(from(credit.poster_path, settingsProvider.getPosterImageWidth()))
                .backdropImage(from(credit.backdrop_path, settingsProvider.getBackdropImageWidth()))
                .build();
    }

    /**
     * @return A {@link TvShowCredit}s mapped from a {@link TmdbTvShowCredit}
     *
     * @throws InvalidTvShowCreditException thrown on error
     */
    public TvShowCredit from(final TmdbTvShowCredit credit) throws InvalidTvShowCreditException {
        return TvShowCredit.newBuilder()
                .id(String.valueOf(credit.id))
                .character(credit.character)
                .name(credit.name)
                .overview(credit.overview)
                .posterImage(from(credit.poster_path, settingsProvider.getPosterImageWidth()))
                .backdropImage(from(credit.backdrop_path, settingsProvider.getBackdropImageWidth()))
                .build();
    }

    /**
     * @return A {@link List} of {@link MovieCredit}s mapped from a {@link TmdbMovieCredits} or an empty list if none available
     *
     * @throws InvalidMovieCreditException thrown on error
     */
    public List<MovieCredit> from(final TmdbMovieCredits credits) throws InvalidMovieCreditException {
        final List<MovieCredit> result = new ArrayList<>();

        if (credits != null && credits.cast != null) {
            for (final TmdbMovieCredit credit : credits.cast) {
                result.add(from(credit));
            }
        }

        return result;
    }

    /**
     * @return A {@link List} of {@link TvShowCredit}s mapped from a {@link TmdbTvCredits} or an empty list if none available
     *
     * @throws InvalidTvShowCreditException thrown on error
     */
    public List<TvShowCredit> from(final TmdbTvCredits credits) throws InvalidTvShowCreditException {
        final List<TvShowCredit> result = new ArrayList<>();

        if (credits != null && credits.cast != null) {
            for (final TmdbTvShowCredit credit : credits.cast) {
                result.add(TvShowCredit.newBuilder()
                        .id(String.valueOf(credit.id))
                        .character(credit.character)
                        .name(credit.name)
                        .overview(credit.overview)
                        .posterImage(from(credit.poster_path, settingsProvider.getPosterImageWidth()))
                        .backdropImage(from(credit.backdrop_path, settingsProvider.getBackdropImageWidth()))
                        .build());
            }
        }

        return result;
    }
    //endregion Credits
}