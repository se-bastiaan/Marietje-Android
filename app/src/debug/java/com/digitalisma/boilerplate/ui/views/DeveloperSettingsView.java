package com.digitalisma.boilerplate.ui.views;

import android.support.annotation.NonNull;

import okhttp3.logging.HttpLoggingInterceptor;

public interface DeveloperSettingsView {
    void changeGitSha(@NonNull String gitSha);

    void changeBuildDate(@NonNull String date);

    void changeBuildVersionCode(@NonNull String versionCode);

    void changeBuildVersionName(@NonNull String versionName);

    void changeStethoState(boolean enabled);

    void changeLeakCanaryState(boolean enabled);

    void changeHttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel);

    void showMessage(@NonNull String message);

    void showAppNeedsToBeRestarted();
}