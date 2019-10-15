package com.glad.watchnext.platform.util;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

/**
 * Created by Gautam Lad
 */
public final class AnimUtil {
    //region Listeners
    public interface StartListener {
        void onStart(@NonNull final Animation animation);
    }
    //endregion Listeners

    public static void create(@NonNull final View view, @AnimRes final int animResource, @NonNull final StartListener listener) {
        new AnimUtil(view, animResource, listener);
    }

    private AnimUtil(@NonNull final View view, @AnimRes final int animResource, @NonNull final StartListener listener) {
        final Animation anim = AnimationUtils.loadAnimation(view.getContext(), animResource);
        anim.setAnimationListener(new AnimationListenerImpl() {
            @Override
            public void onAnimationStart(final Animation animation) {
                listener.onStart(anim);
            }
        });
        view.startAnimation(anim);
    }

    /**
     * Empty implementation of {@link AnimationListener}
     */
    static class AnimationListenerImpl implements AnimationListener {
        @Override
        public void onAnimationStart(final Animation animation) {
        }

        @Override
        public void onAnimationEnd(final Animation animation) {
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {
        }
    }
}
