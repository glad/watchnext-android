package com.glad.watchnext.app.di.module.activity;

import com.glad.watchnext.app.di.scope.PerActivity;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.platform.provider.AndroidNetworkStateProvider;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Gautam Lad
 */
@Module
public final class ActivityModule {
    @NonNull private final AppCompatActivity activity;

    public ActivityModule(@NonNull final AppCompatActivity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    AppCompatActivity providesActivity() {
        return activity;
    }

    @Named ("isListView")
    @PerActivity
    @Provides
    ReplaySubject<Boolean> providesIsListView() {
        final ReplaySubject<Boolean> subject = ReplaySubject.createWithSize(1);
        subject.onNext(false);
        return subject;
    }

    @PerActivity
    @Provides
    NetworkStateProvider providesNetworkStateProvider(
            @NonNull final SchedulerProvider schedulerProvider) {
        return new AndroidNetworkStateProvider(activity, schedulerProvider);
    }
}