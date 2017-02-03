package com.digitalisma.boilerplate.test.common.injection.component;

import com.digitalisma.boilerplate.injection.component.AppComponent;
import com.digitalisma.boilerplate.injection.module.DeveloperSettingsModule;
import com.digitalisma.boilerplate.injection.module.NetworkModule;
import com.digitalisma.boilerplate.injection.module.OkHttpInterceptorsModule;
import com.digitalisma.boilerplate.test.common.injection.module.AppTestModule;
import com.digitalisma.boilerplate.test.common.injection.module.DataTestModule;

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