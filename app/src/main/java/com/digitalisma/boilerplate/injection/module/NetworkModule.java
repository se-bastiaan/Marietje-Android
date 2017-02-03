package com.digitalisma.boilerplate.injection.module;


import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.injection.OkHttpInterceptors;
import com.digitalisma.boilerplate.injection.OkHttpNetworkInterceptors;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient.Builder provideOkHttpClientBuilder(@OkHttpInterceptors @NonNull List<Interceptor> interceptors,
                                                           @OkHttpNetworkInterceptors @NonNull List<Interceptor> networkInterceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS);
        okHttpClientBuilder.retryOnConnectionFailure(true);

        for (Interceptor interceptor : interceptors) {
            okHttpClientBuilder.addInterceptor(interceptor);
        }

        for (Interceptor networkInterceptor : networkInterceptors) {
            okHttpClientBuilder.addNetworkInterceptor(networkInterceptor);
        }

        return okHttpClientBuilder;
    }

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder httpClientBuilder) {
        return httpClientBuilder.build();
    }

}