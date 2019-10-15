package com.glad.watchnext.app.di.module.app;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.app.view.splash.SplashScreen;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Stack;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class AppModule {
    @NonNull private final Application application;

    public AppModule(@NonNull final Application application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    Context providesContext() {
        return application;
    }

    @Singleton
    @Provides
    @Named ("navigation-stack")
    Stack<ScreenContract.Screen> providesNavigationStack() {
        final Stack<ScreenContract.Screen> stack = new Stack<>();
        stack.push(new SplashScreen()); // Populate the stack with the initial screen
        return stack;
    }
}

