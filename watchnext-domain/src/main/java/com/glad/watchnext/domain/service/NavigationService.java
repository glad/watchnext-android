package com.glad.watchnext.domain.service;

import java.util.List;

/**
 * An interface contract for providing navigation
 * <p>
 * Created by Gautam Lad
 */
public interface NavigationService {
    /**
     * @return True if back navigation was successful, false otherwise
     */
    boolean goBack();

    /**
     * @return True if back navigation all the way to root screen was successful, false otherwise
     */
    boolean goBackToRoot();

    /**
     * Navigates to the Settings view
     */
    void navigateToConfig();

    /**
     * Navigates to the Search screen
     */
    void navigateToSearch();

    /**
     * Navigates to the Home screen
     */
    void navigateToHome();

    /**
     * Navigates to the Movie detail screen
     *
     * @param id The id that corresponds to {@link com.glad.watchnext.domain.model.movie.MovieDetailed#id}
     */
    void navigateToMovieDetail(final String id);

    /**
     * Navigates to the Tv Show detail screen
     *
     * @param id The id that corresponds to {@link com.glad.watchnext.domain.model.tv.show.TvShowDetailed#id}
     */
    void navigateToTvShowDetail(final String id);

    /**
     * Navigates to the Person detail screen
     *
     * @param id The id that corresponds to {@link com.glad.watchnext.domain.model.person.PersonDetailed#id}
     */
    void navigateToPersonDetail(final String id);

    /**
     * Navigates to the Image viewer screen
     *
     * @param urls {@link List} of urls
     */
    void navigateToImageViewer(final List<String> urls);
}