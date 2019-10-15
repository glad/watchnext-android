package com.glad.watchnext.app.service;

import com.glad.watchnext.app.view.detail.DetailScreen;
import com.glad.watchnext.app.view.home.HomeScreen;
import com.glad.watchnext.app.view.imageviewer.ImageViewerScreen;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;
import com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel;
import com.glad.watchnext.app.view.search.SearchScreen;
import com.glad.watchnext.app.view.settings.SettingsScreen;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.platform.delegate.navigation.NavigationDelegate;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam Lad
 */
public final class AppNavigationService implements NavigationService {
    @NonNull private final NavigationDelegate delegate;

    public AppNavigationService(@NonNull final NavigationDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean goBack() {
        return delegate.goBack();
    }

    @Override
    public boolean goBackToRoot() {
        return delegate.goBackToRoot();
    }

    @Override
    public void navigateToConfig() {
        delegate.goTo(new SettingsScreen(), false);
    }

    @Override
    public void navigateToSearch() {
        delegate.goTo(new SearchScreen(), false);
    }

    @Override
    public void navigateToHome() {
        delegate.goTo(new HomeScreen(), true);
    }

    @Override
    public void navigateToMovieDetail(@NonNull final String id) {
        delegate.goTo(new DetailScreen(id, MovieDetailedPresentationModel.class.getName()), false);
    }

    @Override
    public void navigateToTvShowDetail(@NonNull final String id) {
        delegate.goTo(new DetailScreen(id, TvShowDetailedPresentationModel.class.getName()), false);
    }

    @Override
    public void navigateToPersonDetail(@NonNull final String id) {
        delegate.goTo(new DetailScreen(id, PersonDetailedPresentationModel.class.getName()), false);
    }

    @Override
    public void navigateToImageViewer(@NonNull final List<String> urls) {
        delegate.goTo(new ImageViewerScreen(new ArrayList<>(urls)), false);
    }
}