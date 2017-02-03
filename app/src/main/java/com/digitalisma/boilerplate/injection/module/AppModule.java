package com.digitalisma.boilerplate.injection.module;

import android.content.Context;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class AppModule {

    protected final DigitalismaApplication application;

    public AppModule(DigitalismaApplication application) {
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
