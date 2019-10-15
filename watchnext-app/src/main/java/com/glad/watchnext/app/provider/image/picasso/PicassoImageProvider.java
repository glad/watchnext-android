package com.glad.watchnext.app.provider.image.picasso;

import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.util.ValueHelper;
import com.glad.watchnext.platform.provider.ImageProvider;
import com.squareup.picasso.Picasso;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import io.reactivex.Completable;

/**
 * Created by Gautam Lad
 */
public final class PicassoImageProvider implements ImageProvider {
    @NonNull private final SchedulerProvider schedulerProvider;

    public PicassoImageProvider(@NonNull final SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Completable load(@NonNull final ImageProvider.Options options) {
        return Completable.create(emitter -> {
            try {
                final ImageView targetView = ValueHelper.requireNonNull(options.getTargetView().get(), "Target image view is null");
                final String url = ValueHelper.requireNonNull(options.getUrl(), "Target image view is null");

                Picasso.get()
                        .load(url)
                        .placeholder(options.getPlaceholder())
                        .into(targetView);
            } catch (final Throwable t) {
                emitter.onError(t);
            }
        }).subscribeOn(schedulerProvider.ui());
    }
}