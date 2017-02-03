package eu.se_bastiaan.marietje.injection.module;

import android.support.annotation.NonNull;

import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.ui.presenters.DeveloperSettingsFragPresenter;
import com.github.pedrovgs.lynx.LynxConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.marietje.devsettings.DeveloperSettingsModelImpl;
import eu.se_bastiaan.marietje.devsettings.LeakCanaryProxy;
import eu.se_bastiaan.marietje.devsettings.LeakCanaryProxyImpl;
import eu.se_bastiaan.marietje.ui.other.ViewModifier;
import hu.supercluster.paperwork.Paperwork;
import eu.se_bastiaan.marietje.devsettings.DeveloperSettings;
import eu.se_bastiaan.marietje.devsettings.DeveloperSettingsModel;
import eu.se_bastiaan.marietje.devsettings.MainActivityViewModifier;
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
    public DeveloperSettings provideDeveloperSettings(@NonNull MarietjeApp app) {
        return new DeveloperSettings(app.getSharedPreferences("developer_settings", MODE_PRIVATE));
    }

    @Provides
    @NonNull
    @Singleton
    public LeakCanaryProxy provideLeakCanaryProxy(@NonNull MarietjeApp app) {
        return new LeakCanaryProxyImpl(app);
    }

    @Provides
    @NonNull
    @Singleton
    public Paperwork providePaperwork(@NonNull MarietjeApp app) {
        return new Paperwork(app);
    }


    // We will use this concrete type for debug code, but main code will see only DeveloperSettingsModel interface.
    @Provides
    @NonNull
    @Singleton
    public DeveloperSettingsModelImpl provideDeveloperSettingsModelImpl(@NonNull MarietjeApp app, @NonNull DeveloperSettings developerSettings,
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