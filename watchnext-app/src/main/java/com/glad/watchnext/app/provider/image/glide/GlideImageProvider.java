package com.glad.watchnext.app.provider.image.glide;

import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.util.ValueHelper;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;

/**
 * This class exists here because starting with v4 of Glide, the {@link com.bumptech.glide.module.AppGlideModule}
 * cannot exist inside a library project.
 * <p>
 * Created by Gautam Lad
 *
 * @see "https://bumptech.github.io/glide/doc/configuration.html#avoid-appglidemodule-in-libraries"
 */
public final class GlideImageProvider implements ImageProvider {
    @NonNull private final WeakReference<Context> contextReference;
    @NonNull private final SchedulerProvider schedulerProvider;

    public GlideImageProvider(
            @NonNull final Context context,
            @NonNull final SchedulerProvider schedulerProvider) {
        this.contextReference = new WeakReference<>(context);
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Completable load(@NonNull final ImageProvider.Options options) {
        return Completable.create(emitter -> {
            try {
                final Context context = ValueHelper.requireNonNull(contextReference.get(), "Context is null");
                final ImageView targetView = ValueHelper.requireNonNull(options.getTargetView().get(), "Target image view is null");
                final String url = ValueHelper.requireNonNull(options.getUrl(), "Target image view is null");

                GlideApp.with(context)
                        .load(url)
                        .placeholder(options.getPlaceholder())
                        .into(targetView);
            } catch (final Throwable t) {
                emitter.onError(t);
            }
        }).subscribeOn(schedulerProvider.ui());
    }
}
