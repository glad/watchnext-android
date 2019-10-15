package com.glad.watchnext.app.view.settings;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.domain.model.common.Settings.ImageQualityType;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @NonNull public static final String FRAGMENT_TAG = "SettingsFragment";
    @Inject SettingsProvider settingsProvider;
    @Inject SchedulerProvider schedulerProvider;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.inject(this);
        getPreferenceManager().setSharedPreferencesName(settingsProvider.getAppVersion());
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = view.getContext();
        final PreferenceScreen screen = getPreferenceScreen();

        //region Cache Settings
        final PreferenceCategory cacheCategory = (PreferenceCategory) screen.findPreference(getString(R.string.cache_category_key));
        // Max Cache Age
        disposables.add(addListPreference(context, cacheCategory,
                SettingsProvider.MAX_CACHE_AGE_MS_KEY,
                getString(R.string.cache_age_title),
                getString(R.string.cache_age_summary),
                String.valueOf(settingsProvider.getMaxCacheAgeMs()),
                String.valueOf(5L * 60000L),
                new String[]{"5-min", // Used for debugging
                        "1 hr", "6 hrs", "12 hrs",
                        "1-day", "1-week", "1-month",
                        "Forever"},
                new String[]{String.valueOf(5L * 60000L), // Used for debugging
                        String.valueOf(3600000L), String.valueOf(6L * 3600000L), String.valueOf(12L * 3600000L),
                        String.valueOf(86400000L), String.valueOf(7L * 86400000L), String.valueOf(31L * 86400000L),
                        String.valueOf(Long.MAX_VALUE)
                })
                .subscribe(value -> settingsProvider
                        .setMaxCacheAgeMs(Long.parseLong(value))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .onErrorComplete()
                        .subscribe()));

        // Max Cache Age
        disposables.add(addListPreference(context, cacheCategory,
                SettingsProvider.MAX_CACHE_SIZE_BYTES_KEY,
                getString(R.string.cache_size_title),
                getString(R.string.cache_size_summary),
                String.valueOf(settingsProvider.getMaxCacheSizeBytes()),
                String.valueOf(1048576L),
                new String[]{"1 MB", "5 MB", "10 MB", "25 MB", "50 MB", "100 MB"},
                new String[]{
                        String.valueOf(1048576L),
                        String.valueOf(5L * 1048576L),
                        String.valueOf(10L * 1048576L),
                        String.valueOf(25L * 1048576L),
                        String.valueOf(50L * 1048576L),
                        String.valueOf(100L * 1048576L)
                })
                .subscribe(value -> settingsProvider.setMaxCacheSizeBytes(Long.parseLong(value))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .onErrorComplete()
                        .subscribe()));

        //endregion Cache Settings

        //region Miscellaneous Settings
        final PreferenceCategory miscCategory = (PreferenceCategory) screen.findPreference(getString(R.string.misc_category_key));

        // Image Quality
        disposables.add(addListPreference(context, miscCategory,
                SettingsProvider.IMAGE_QUALITY_KEY,
                getString(R.string.image_quality_title),
                getString(R.string.image_quality_summary),
                settingsProvider.getImageQuality().toString(),
                ImageQualityType.MEDIUM.name(),
                ImageQualityType.stringValues(),
                ImageQualityType.stringValues())
                .subscribe(value -> settingsProvider.setImageQuality(ImageQualityType.valueOf(value))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .onErrorComplete()
                        .subscribe()));
        //endregion Miscellaneous Settings
    }

    @NonNull
    private Observable<String> addListPreference(@NonNull final Context context, @NonNull final PreferenceCategory category,
            @NonNull final String key, @NonNull final String title, @NonNull final String summary,
            @NonNull final String savedValue, @NonNull final String defaultValue,
            @NonNull final String[] entries, @NonNull final String[] values) {
        return Observable.create(emitter -> {
            final ListPreference list = new ListPreference(context);
            list.setKey(key);
            list.setTitle(title);
            list.setSummary(summary);
            list.setEntries(entries);
            list.setEntryValues(values);
            list.setOnPreferenceChangeListener((preference, newValue) -> {
                final int index = list.findIndexOfValue(newValue.toString());
                preference.setTitle(String.format("%s (%s)", title, entries[index]));
                emitter.onNext(newValue.toString());
                return true;
            });
            category.addPreference(list);

            final int savedIndex = list.findIndexOfValue(savedValue);
            if (savedIndex != -1) {
                list.setValue(savedValue);
                list.callChangeListener(savedValue);
            } else {
                list.setValue(defaultValue);
                list.callChangeListener(defaultValue);
            }
        });
    }
}