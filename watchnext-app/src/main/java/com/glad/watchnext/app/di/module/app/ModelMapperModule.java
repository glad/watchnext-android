package com.glad.watchnext.app.di.module.app;

import com.glad.watchnext.app.view.model.util.PresentationModelMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class ModelMapperModule {
    @Singleton
    @Provides
    PresentationModelMapper providesPresentationModelMapper() {
        return new PresentationModelMapper();
    }
}

