package com.digitalisma.boilerplate.injection.module;

import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.data.local.PreferencesHelper;
import com.digitalisma.boilerplate.data.remote.DynamicUrlInterceptor;
import com.digitalisma.boilerplate.injection.OkHttpInterceptors;
import com.digitalisma.boilerplate.injection.OkHttpNetworkInterceptors;

import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

import static java.util.Collections.emptyList;

/**
 * Provides OkHttp interceptors for release build.
 */
@Module
public class OkHttpInterceptorsModule {

    @Provides
    @Singleton
    @NonNull
    public DynamicUrlInterceptor provideDynamicBaseUrlInterceptor(final PreferencesHelper preferencesHelper) {
        return new DynamicUrlInterceptor(preferencesHelper);
    }

    @Provides
    @OkHttpInterceptors
    @Singleton
    @NonNull
    public List<Interceptor> provideOkHttpInterceptors(DynamicUrlInterceptor urlInterceptor) {
        return Collections.singletonList(urlInterceptor);
    }

    @Provides
    @OkHttpNetworkInterceptors
    @Singleton
    @NonNull
    public List<Interceptor> provideOkHttpNetworkInterceptors() {
        return emptyList();
    }

}