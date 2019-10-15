package com.glad.watchnext.app.view.model.util;

import com.glad.watchnext.app.view.model.common.CastCreditPresentationModel;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.person.MovieCreditPresentationModel;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;
import com.glad.watchnext.app.view.model.person.PersonSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.person.TvShowCreditPresentationModel;
import com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel;
import com.glad.watchnext.app.view.model.tv.show.TvShowSimplifiedPresentationModel;
import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.exception.InvalidMovieCreditException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.exception.InvalidTvShowCreditException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.CastCredit;
import com.glad.watchnext.domain.model.common.Category;
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
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A collection of methods responsible for mapping domain models to presentation models
 * <p>
 * Created by Gautam Lad
 */
public class PresentationModelMapper {
    @NonNull
    public CollectionPresentationModel from(@NonNull final Category val) throws InvalidCategoryException {
        return CollectionPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .build();
    }

    @NonNull
    public CastCreditPresentationModel from(@NonNull final CastCredit val) throws InvalidCastException {
        return CastCreditPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .character(val.getCharacter())
                .profileImageUrl(from(val.getProfileImage()))
                .build();
    }

    //region Movie
    @NonNull
    public MovieSimplifiedPresentationModel from(@NonNull final MovieSimplified val) throws InvalidMovieException {
        return MovieSimplifiedPresentationModel.newBuilder()
                .id(val.getId())
                .title(val.getTitle())
                .overview(val.getOverview())
                .posterImageUrl(from(val.getPosterImage()))
                .backdropImageUrl(from(val.getBackdropImage()))
                .build();
    }

    @NonNull
    public MovieDetailedPresentationModel from(@NonNull final MovieDetailed val) throws InvalidMovieException {
        final int releaseYear;
        if (val.getReleaseDate() != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(val.getReleaseDate());
            releaseYear = cal.get(Calendar.YEAR);
        } else {
            releaseYear = 0;
        }

        return MovieDetailedPresentationModel.newBuilder()
                .id(val.getId())
                .title(val.getTitle())
                .overview(val.getOverview())
                .posterImageUrls(fromImages(val.getPosterImages()))
                .backdropImageUrls(fromImages(val.getBackdropImages()))
                .releaseYear(releaseYear)
                .certification(val.getCertification())
                .countryCode(val.getCountryCode())
                .runtimeMs(val.getRuntimeMinutes())
                .genres(fromGenres(val.getGenres()))
                .build();
    }
    //endregion Movie

    //region TvShow
    @NonNull
    public TvShowSimplifiedPresentationModel from(@NonNull final TvShowSimplified val) throws InvalidTvShowException {
        return TvShowSimplifiedPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .overview(val.getOverview())
                .posterImageUrl(from(val.getPosterImage()))
                .backdropImageUrl(from(val.getBackdropImage()))
                .build();
    }

    @NonNull
    public TvShowDetailedPresentationModel from(@NonNull final TvShowDetailed val) throws InvalidTvShowException {
        return TvShowDetailedPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .overview(val.getOverview())
                .network(val.getNetwork())
                .rating(val.getRating())
                .countryCode(val.getCountryCode())
                .runtimeMs(val.getRuntimeMinutes())
                .genres(fromGenres(val.getGenres()))
                .posterImageUrls(fromImages(val.getPosterImages()))
                .backdropImageUrls(fromImages(val.getBackdropImages()))
                .build();
    }
    //endregion TvShow

    //region Person
    @NonNull
    public PersonSimplifiedPresentationModel from(@NonNull final PersonSimplified val) throws InvalidPersonException {
        return PersonSimplifiedPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .knownFor(val.getKnownFor())
                .profileImageUrl(from(val.getProfileImage()))
                .build();
    }

    @NonNull
    public PersonDetailedPresentationModel from(@NonNull final PersonDetailed val) throws InvalidPersonException {
        final List<String> backdropImages = new ArrayList<>();
        {
            final List<MovieCreditPresentationModel> credits = fromMovieCredits(val.getMovieCredits());
            for (final MovieCreditPresentationModel credit : credits) {
                backdropImages.add(credit.getBackdropImageUrl());
            }
        }
        {
            final List<TvShowCreditPresentationModel> credits = fromTvShowCredits(val.getTvShowCredits());
            for (final TvShowCreditPresentationModel credit : credits) {
                backdropImages.add(credit.getBackdropImageUrl());
            }
        }

        return PersonDetailedPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .birthDate(val.getBirthDate())
                .deathDate(val.getDeathDate())
                .birthPlace(val.getBirthPlace())
                .biography(val.getBiography())
                .profileImageUrls(fromImages(val.getProfileImages()))
                .backdropImageUrls(backdropImages)
                .build();
    }
    //endregion Person

    //region Genre
    @NonNull
    private List<String> fromGenres(@NonNull final List<Genre> list) {
        final List<String> result = new ArrayList<>();
        for (final Genre genre : list) {
            result.add(genre.getName());
        }
        return result;
    }

    //endregion Genre

    //region Credits

    @NonNull
    private List<MovieCreditPresentationModel> fromMovieCredits(@NonNull final List<MovieCredit> list) {
        final List<MovieCreditPresentationModel> result = new ArrayList<>();
        for (final MovieCredit credit : list) {
            try {
                result.add(from(credit));
            } catch (final InvalidMovieCreditException e) {
                // No-op
            }
        }
        return result;
    }

    @NonNull
    public MovieCreditPresentationModel from(@NonNull final MovieCredit val) throws InvalidMovieCreditException {
        return MovieCreditPresentationModel.newBuilder()
                .id(val.getId())
                .title(val.getTitle())
                .overview(val.getOverview())
                .posterImageUrl(from(val.getPosterImage()))
                .backdropImageUrl(from(val.getBackdropImage()))
                .character(val.getCharacter())
                .build();
    }

    @NonNull
    private List<TvShowCreditPresentationModel> fromTvShowCredits(@NonNull final List<TvShowCredit> list) {
        final List<TvShowCreditPresentationModel> result = new ArrayList<>();
        for (final TvShowCredit credit : list) {
            try {
                result.add(from(credit));
            } catch (final InvalidTvShowCreditException e) {
                // No-op
            }
        }
        return result;
    }

    @NonNull
    public TvShowCreditPresentationModel from(@NonNull final TvShowCredit val) throws InvalidTvShowCreditException {
        return TvShowCreditPresentationModel.newBuilder()
                .id(val.getId())
                .name(val.getName())
                .overview(val.getOverview())
                .posterImageUrl(from(val.getPosterImage()))
                .backdropImageUrl(from(val.getBackdropImage()))
                .character(val.getCharacter())
                .build();
    }
    //endregion Credits

    //region Image
    @NonNull
    public String from(@Nullable final Image image) {
        return ValueHelper.nullToEmpty((image == null) ? null : image.getUrl());
    }

    @NonNull
    private List<String> fromImages(@NonNull final List<Image> list) {
        final List<String> result = new ArrayList<>();
        for (final Image image : list) {
            final String url = from(image);
            if (!url.isEmpty()) {
                result.add(from(image));
            }
        }

        return result;
    }
    //endregion Image
}