package com.digitalisma.boilerplate.injection.component;

import android.content.Context;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.data.DataManager;
import com.digitalisma.boilerplate.data.local.PreferencesHelper;
import com.digitalisma.boilerplate.data.remote.PersonsService;
import com.digitalisma.boilerplate.devsettings.LeakCanaryProxy;
import com.digitalisma.boilerplate.injection.ApplicationContext;
import com.digitalisma.boilerplate.injection.module.AppModule;
import com.digitalisma.boilerplate.injection.module.DataModule;
import com.digitalisma.boilerplate.injection.module.DeveloperSettingsModule;
import com.digitalisma.boilerplate.injection.module.NetworkModule;
import com.digitalisma.boilerplate.injection.module.OkHttpInterceptorsModule;
import com.digitalisma.boilerplate.util.EventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        OkHttpInterceptorsModule.class,
        NetworkModule.class,
        DataModule.class,
        DeveloperSettingsModule.class
})
public interface AppComponent {

    @ApplicationContext Context context();
    DigitalismaApplication application();
    PersonsService personsService();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    EventBus eventBus();
    LeakCanaryProxy leakCanaryProxy();
    DeveloperSettingsComponent plusDeveloperSettingsComponent();

}
