package com.glad.watchnext.app.di.module.activity;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.scope.PerActivity;
import com.glad.watchnext.app.service.AppNavigationService;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.platform.delegate.navigation.AndroidNavigationDelegate;
import com.glad.watchnext.platform.delegate.navigation.NavigationDelegate;
import com.glad.watchnext.platform.view.ScreenContract;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public class NavigationModule {
    @PerActivity
    @Provides
    NavigationDelegate providesNavigationDelegate(
            @NonNull final AppCompatActivity activity,
            @NonNull @Named ("navigation-stack") final Stack<ScreenContract.Screen> screenStack,
            @NonNull final LogService log) {
        return new AndroidNavigationDelegate(activity, R.id.screen_container, screenStack, log);
    }

    @PerActivity
    @Provides
    NavigationService providesNavigationService(@NonNull final NavigationDelegate navigationDelegate) {
        return new AppNavigationService(navigationDelegate);
    }
}