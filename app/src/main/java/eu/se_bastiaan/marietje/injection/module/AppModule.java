package eu.se_bastiaan.marietje.injection.module;

import android.content.Context;

import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class AppModule {

    protected final MarietjeApp application;

    public AppModule(MarietjeApp application) {
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
