package eu.se_bastiaan.marietje.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import eu.se_bastiaan.marietje.injection.component.AppComponent;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.injection.module.NetworkModule;
import eu.se_bastiaan.marietje.injection.module.OkHttpInterceptorsModule;
import eu.se_bastiaan.marietje.test.common.injection.module.AppTestModule;
import eu.se_bastiaan.marietje.test.common.injection.module.DataTestModule;

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