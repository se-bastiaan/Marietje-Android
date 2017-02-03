package eu.se_bastiaan.marietje.test.common.injection.component;

import eu.se_bastiaan.marietje.injection.component.AppComponent;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.injection.module.NetworkModule;
import eu.se_bastiaan.marietje.injection.module.OkHttpInterceptorsModule;
import eu.se_bastiaan.marietje.test.common.injection.module.AppTestModule;
import eu.se_bastiaan.marietje.test.common.injection.module.DataTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppTestModule.class,
        DataTestModule.class,
        OkHttpInterceptorsModule.class,
        NetworkModule.class,
        DeveloperSettingsModule.class
})
public interface TestComponent extends AppComponent {

}