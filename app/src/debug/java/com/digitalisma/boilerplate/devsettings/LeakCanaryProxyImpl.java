package com.digitalisma.boilerplate.devsettings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryProxyImpl implements LeakCanaryProxy {

    @NonNull
    private final DigitalismaApplication app;

    @Nullable
    private RefWatcher refWatcher;

    public LeakCanaryProxyImpl(@NonNull DigitalismaApplication app) {
        this.app = app;
    }

    @Override
    public void init() {
        refWatcher = LeakCanary.install(app);
    }

    @Override
    public void watch(@NonNull Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }
}