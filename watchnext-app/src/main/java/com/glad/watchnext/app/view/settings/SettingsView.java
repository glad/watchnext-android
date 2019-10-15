package com.glad.watchnext.app.view.settings;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.util.ValueHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gautam Lad
 */
public final class SettingsView extends FrameLayout implements SettingsContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(SettingsView.class);

    @Inject SettingsContract.Presenter presenter;
    @Inject LogService log;
    @Inject AppCompatActivity activity;

    @BindView (R.id.toolbar) Toolbar toolbar;

    @Nullable private AlertDialog resetConfirmationDialog;

    //region Constructor
    public SettingsView(@NonNull final Context context) {
        this(context, null);
    }

    public SettingsView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SettingsView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_settings, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = ValueHelper.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);
        createPreferenceFragment();
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        presenter.subscribe();
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        cancelResetConfirmationDialog();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        cancelResetConfirmationDialog();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region SettingsContract.View
    @Override
    public void onSettingsReset() {
        createPreferenceFragment();
    }
    //endregion SettingsContract.View

    /**
     * Resets the settings to the default values (called by {@link SettingsScreen}
     */
    void resetSettings() {
        final Context context = getContext();
        resetConfirmationDialog = new AlertDialog.Builder(context, R.style.DialogTheme)
                .setMessage(context.getString(R.string.reset_confirmation_message))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> presenter.resetSettings())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    // No-op
                }).show();
    }

    /**
     * Cancels the reset confirmation dialog if it's open
     */
    void cancelResetConfirmationDialog() {
        if (resetConfirmationDialog != null) {
            resetConfirmationDialog.cancel();
            resetConfirmationDialog = null;
        }
    }

    /**
     * Create the preference fragment
     */
    private void createPreferenceFragment() {
        final FragmentManager fm = activity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        // If the preference already exists, remove it
        final Fragment prev = fm.findFragmentByTag(SettingsFragment.FRAGMENT_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.replace(R.id.fragment_container, new SettingsFragment(), SettingsFragment.FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }
}