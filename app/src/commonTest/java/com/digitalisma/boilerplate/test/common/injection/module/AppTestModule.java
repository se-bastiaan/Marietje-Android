package com.digitalisma.boilerplate.test.common.injection.module;

import android.content.Context;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class AppTestModule {

    private final DigitalismaApplication application;

    public AppTestModule(DigitalismaApplication application) {
        this.application = application;
    }

    @Provides
    DigitalismaApplication provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

}
