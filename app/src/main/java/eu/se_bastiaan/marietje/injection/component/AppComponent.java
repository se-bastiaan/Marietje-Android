package eu.se_bastiaan.marietje.injection.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.devsettings.LeakCanaryProxy;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.module.AppModule;
import eu.se_bastiaan.marietje.injection.module.DataModule;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.injection.module.NetworkModule;
import eu.se_bastiaan.marietje.injection.module.OkHttpInterceptorsModule;
import eu.se_bastiaan.marietje.util.EventBus;

@Singleton
@Component(modules = {
        AppModule.class,
        OkHttpInterceptorsModule.class,
        NetworkModule.class,
        DataModule.class,
        DeveloperSettingsModule.class
})
public interface AppComponent {

    @ApplicationContext
    Context context();
    MarietjeApp application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    EventBus eventBus();
    LeakCanaryProxy leakCanaryProxy();
    DeveloperSettingsComponent plusDeveloperSettingsComponent();

}
