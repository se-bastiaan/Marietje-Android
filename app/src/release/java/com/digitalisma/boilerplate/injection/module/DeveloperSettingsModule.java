package com.digitalisma.boilerplate.injection.module;

import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.devsettings.DeveloperSettingsModel;
import com.digitalisma.boilerplate.devsettings.LeakCanaryProxy;
import com.digitalisma.boilerplate.devsettings.NoOpLeakCanaryProxy;
import com.digitalisma.boilerplate.ui.other.NoOpViewModifier;
import com.digitalisma.boilerplate.ui.other.ViewModifier;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DeveloperSettingsModule {

    @NonNull
    public static final String MAIN_ACTIVITY_VIEW_MODIFIER = "main_activity_view_modifier";

    @Provides
    @NonNull @Named(MAIN_ACTIVITY_VIEW_MODIFIER)
    public ViewModifier provideMainActivityViewModifier() {
        return new NoOpViewModifier();
    }

    @Provides
    @NonNull
    public DeveloperSettingsModel provideDeveloperSettingsModel() {
        return new DeveloperSettingsModel() {
            @Override
            public void apply() {
            }
        };
    }

    @Provides
    @NonNull
    @Singleton
    public LeakCanaryProxy provideLeakCanaryProxy() {
        return new NoOpLeakCanaryProxy();
    }

}