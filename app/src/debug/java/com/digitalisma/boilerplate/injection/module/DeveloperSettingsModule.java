package com.digitalisma.boilerplate.injection.module;

import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.ui.presenters.DeveloperSettingsFragPresenter;
import com.github.pedrovgs.lynx.LynxConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.supercluster.paperwork.Paperwork;
import com.digitalisma.boilerplate.devsettings.DeveloperSettings;
import com.digitalisma.boilerplate.devsettings.DeveloperSettingsModel;
import com.digitalisma.boilerplate.devsettings.DeveloperSettingsModelImpl;
import com.digitalisma.boilerplate.devsettings.LeakCanaryProxy;
import com.digitalisma.boilerplate.devsettings.LeakCanaryProxyImpl;
import com.digitalisma.boilerplate.devsettings.MainActivityViewModifier;
import com.digitalisma.boilerplate.ui.other.ViewModifier;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.content.Context.MODE_PRIVATE;

@Module
public class DeveloperSettingsModule {

    @NonNull
    public static final String MAIN_ACTIVITY_VIEW_MODIFIER = "main_activity_view_modifier";

    @Provides
    @NonNull
    @Named(MAIN_ACTIVITY_VIEW_MODIFIER)
    public ViewModifier provideMainActivityViewModifier() {
        return new MainActivityViewModifier();
    }

    @Provides
    @NonNull
    public DeveloperSettingsModel provideDeveloperSettingsModel(@NonNull DeveloperSettingsModelImpl developerSettingsModelImpl) {
        return developerSettingsModelImpl;
    }

    @Provides
    @NonNull
    @Singleton
    public DeveloperSettings provideDeveloperSettings(@NonNull DigitalismaApplication app) {
        return new DeveloperSettings(app.getSharedPreferences("developer_settings", MODE_PRIVATE));
    }

    @Provides
    @NonNull
    @Singleton
    public LeakCanaryProxy provideLeakCanaryProxy(@NonNull DigitalismaApplication app) {
        return new LeakCanaryProxyImpl(app);
    }

    @Provides
    @NonNull
    @Singleton
    public Paperwork providePaperwork(@NonNull DigitalismaApplication app) {
        return new Paperwork(app);
    }


    // We will use this concrete type for debug code, but main code will see only DeveloperSettingsModel interface.
    @Provides
    @NonNull
    @Singleton
    public DeveloperSettingsModelImpl provideDeveloperSettingsModelImpl(@NonNull DigitalismaApplication app, @NonNull DeveloperSettings developerSettings,
                                                                        @NonNull HttpLoggingInterceptor httpLoggingInterceptor, @NonNull LeakCanaryProxy leakCanaryProxy,
                                                                        @NonNull Paperwork paperwork) {
        return new DeveloperSettingsModelImpl(app, developerSettings, httpLoggingInterceptor, leakCanaryProxy, paperwork);
    }

    @Provides
    @NonNull
    public DeveloperSettingsFragPresenter provideDevSettingsPresenter(@NonNull DeveloperSettingsModelImpl developerSettingsModel) {
        return new DeveloperSettingsFragPresenter(developerSettingsModel);
    }

    @NonNull
    @Provides
    public LynxConfig provideLynxConfig() {
        return new LynxConfig();
    }

}