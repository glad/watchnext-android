package com.glad.watchnext.app;

import com.glad.watchnext.app.di.Injector;

import android.app.Application;

/**
 * Created by Gautam Lad
 */
public final class WatchNextApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.init(this);
    }
}