package com.digitalisma.boilerplate.devsettings;

import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.BuildConfig;
import com.digitalisma.boilerplate.DigitalismaApplication;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.timber.StethoTree;

import java.util.concurrent.atomic.AtomicBoolean;

import hu.supercluster.paperwork.Paperwork;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class DeveloperSettingsModelImpl implements DeveloperSettingsModel {

    @NonNull
    private final DigitalismaApplication app;

    @NonNull
    private final DeveloperSettings developerSettings;

    @NonNull
    private final HttpLoggingInterceptor httpLoggingInterceptor;

    @NonNull
    private final LeakCanaryProxy leakCanaryProxy;

    @NonNull
    private final Paperwork paperwork;

    @NonNull
    private AtomicBoolean stethoAlreadyEnabled = new AtomicBoolean();

    @NonNull
    private AtomicBoolean leakCanaryAlreadyEnabled = new AtomicBoolean();

    public DeveloperSettingsModelImpl(@NonNull DigitalismaApplication app, @NonNull DeveloperSettings developerSettings,
                                      @NonNull HttpLoggingInterceptor httpLoggingInterceptor, @NonNull LeakCanaryProxy leakCanaryProxy,
                                      @NonNull Paperwork paperwork) {
        this.app = app;
        this.developerSettings = developerSettings;
        this.httpLoggingInterceptor = httpLoggingInterceptor;
        this.leakCanaryProxy = leakCanaryProxy;
        this.paperwork = paperwork;

        Timber.plant(new StethoTree());
    }

    @NonNull
    public String getGitSha() {
        return paperwork.get("gitSha");
    }

    @NonNull
    public String getBuildDate() {
        return paperwork.get("buildDate");
    }

    @NonNull
    public String getBuildVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @NonNull
    public String getBuildVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isStethoEnabled() {
        return developerSettings.isStethoEnabled();
    }

    public void changeStethoState(boolean enabled) {
        developerSettings.saveIsStethoEnabled(enabled);
        apply();
    }

    public boolean isLeakCanaryEnabled() {
        return developerSettings.isLeakCanaryEnabled();
    }

    public void changeLeakCanaryState(boolean enabled) {
        developerSettings.saveIsLeakCanaryEnabled(enabled);
        apply();
    }

    @NonNull
    public HttpLoggingInterceptor.Level getHttpLoggingLevel() {
        return developerSettings.getHttpLoggingLevel();
    }

    public void changeHttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel) {
        developerSettings.saveHttpLoggingLevel(loggingLevel);
        apply();
    }

    @Override
    public void apply() {
        // Stetho can not be enabled twice.
        if (stethoAlreadyEnabled.compareAndSet(false, true) && isStethoEnabled()) {
            Stetho.initializeWithDefaults(app);
        }

        // LeakCanary can not be enabled twice.
        if (leakCanaryAlreadyEnabled.compareAndSet(false, true) && isLeakCanaryEnabled()) {
            leakCanaryProxy.init();
        }

        httpLoggingInterceptor.setLevel(developerSettings.getHttpLoggingLevel());
    }

}