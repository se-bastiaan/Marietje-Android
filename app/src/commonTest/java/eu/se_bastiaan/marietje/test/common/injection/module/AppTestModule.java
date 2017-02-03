package eu.se_bastiaan.marietje.test.common.injection.module;

import android.content.Context;

import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class AppTestModule {

    private final MarietjeApp application;

    public AppTestModule(MarietjeApp application) {
        this.application = application;
    }

    @Provides
    MarietjeApp provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

}
