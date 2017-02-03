package com.digitalisma.boilerplate.test.common.injection.module;

import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.data.DataManager;
import com.digitalisma.boilerplate.data.remote.PersonsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class DataTestModule {

    // Mocks

    @Provides
    @NonNull
    @Singleton
    DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @NonNull
    @Singleton
    PersonsService providePersonsService() {
        return mock(PersonsService.class);
    }

}
