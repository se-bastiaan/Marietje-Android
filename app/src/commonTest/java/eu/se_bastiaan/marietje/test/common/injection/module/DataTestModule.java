package eu.se_bastiaan.marietje.test.common.injection.module;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.marietje.data.ControlDataManager;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.SongsDataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.remote.ControlService;
import eu.se_bastiaan.marietje.data.remote.SongsService;

import static org.mockito.Mockito.mock;

@Module
public class DataTestModule {

    // Mocks

    @Provides
    @NonNull
    @Singleton
    DataManager provideDataManager(SongsDataManager songsDataManager, ControlDataManager controlDataManager) {
        return new DataManager(songsDataManager, controlDataManager, mock(PreferencesHelper.class));
    }

    @Provides
    @NonNull
    @Singleton
    ControlDataManager provideControlDataManager() {
        return mock(ControlDataManager.class);
    }

    @Provides
    @NonNull
    @Singleton
    SongsDataManager provideSongDataManager() {
        return mock(SongsDataManager.class);
    }

    @Provides
    @NonNull
    @Singleton
    ControlService provideControlService() {
        return mock(ControlService.class);
    }

    @Provides
    @NonNull
    @Singleton
    SongsService provideSongsService() {
        return mock(SongsService.class);
    }

}
