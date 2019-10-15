package com.glad.watchnext.platform.provider;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;

/**
 * Created by Gautam Lad
 */
public interface ImageProvider {
    Completable load(@NonNull final ImageProvider.Options options);

    //region Options
    final class Options {
        @NonNull private final String url;
        @NonNull private final WeakReference<ImageView> targetView;
        @DrawableRes private final int placeholder;

        @NonNull
        public static Builder newBuilder() {
            return new Builder();
        }

        private Options(final Builder builder) {
            url = builder.url;
            targetView = builder.targetView;
            placeholder = builder.placeholder;
        }

        @NonNull
        public String getUrl() {
            return url;
        }

        @NonNull
        public WeakReference<ImageView> getTargetView() {
            return targetView;
        }

        @DrawableRes
        public int getPlaceholder() {
            return placeholder;
        }

        //region Builder
        public static final class Builder {
            @NonNull private String url = "";
            @NonNull private WeakReference<ImageView> targetView = new WeakReference<>(null);
            @DrawableRes private int placeholder;

            private Builder() {
            }

            public Builder url(final String val) {
                url = val;
                return this;
            }

            @NonNull
            public Builder targetView(@NonNull final ImageView val) {
                targetView = new WeakReference<>(val);
                return this;
            }

            @NonNull
            public Builder placeholder(@DrawableRes final int val) {
                placeholder = val;
                return this;
            }

            @NonNull
            public Options build() {
                return new Options(this);
            }
        }
        //endregion Builder
    }
    //endregion Options
}
