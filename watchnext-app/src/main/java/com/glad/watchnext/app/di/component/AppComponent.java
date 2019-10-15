package com.glad.watchnext.app.di.component;

import com.glad.watchnext.app.di.module.app.AppModule;
import com.glad.watchnext.app.di.module.app.DomainUseCaseModule;
import com.glad.watchnext.app.di.module.app.ModelMapperModule;
import com.glad.watchnext.app.di.module.app.PresentationUseCaseModule;
import com.glad.watchnext.app.di.module.app.ProviderModule;
import com.glad.watchnext.app.di.module.app.AppServiceModule;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Gautam Lad
 */
@Singleton
@Component (modules = {
        AppModule.class,
        ProviderModule.class,
        AppServiceModule.class,
        DomainUseCaseModule.class,
        PresentationUseCaseModule.class,
        ModelMapperModule.class})
public interface AppComponent {
    void inject(Application application);

    ActivityComponent.Builder activityComponentBuilder();

    @Component.Builder
    interface Builder {
        Builder appModule(AppModule module);
        Builder serviceModule(AppServiceModule module);
        Builder mapperModule(ModelMapperModule module);
        Builder providerModule(ProviderModule module);
        Builder useCaseModule(DomainUseCaseModule module);
        Builder presentationUseCaseModule(PresentationUseCaseModule module);
        AppComponent build();
    }
}
