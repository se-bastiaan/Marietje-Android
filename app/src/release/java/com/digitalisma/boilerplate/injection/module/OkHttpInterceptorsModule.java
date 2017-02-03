package eu.se_bastiaan.marietje.injection.module;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.marietje.data.remote.interceptor.CookiesInterceptor;
import eu.se_bastiaan.marietje.data.remote.interceptor.DynamicUrlInterceptor;
import eu.se_bastiaan.marietje.injection.OkHttpInterceptors;
import eu.se_bastiaan.marietje.injection.OkHttpNetworkInterceptors;
import okhttp3.Interceptor;

import static java.util.Collections.emptyList;

/**
 * Provides OkHttp interceptors for release build.
 */
@Module
public class OkHttpInterceptorsModule {

    @Provides
    @OkHttpInterceptors
    @Singleton
    @NonNull
    public List<Interceptor> provideOkHttpInterceptors(DynamicUrlInterceptor urlInterceptor, CookiesInterceptor cookiesInterceptor) {
        return Arrays.asList(urlInterceptor, cookiesInterceptor);
    }

    @Provides
    @OkHttpNetworkInterceptors
    @Singleton
    @NonNull
    public List<Interceptor> provideOkHttpNetworkInterceptors() {
        return emptyList();
    }

}