package com.digitalisma.boilerplate.injection.module;


import android.support.annotation.NonNull;

import com.digitalisma.boilerplate.BuildConfig;
import com.digitalisma.boilerplate.data.remote.PersonsService;
import com.digitalisma.boilerplate.util.GsonTypeAdapterFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class DataModule {

    @Provides
    @NonNull
    @Singleton
    public Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.build();
    }

    @Provides
    @NonNull
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .validateEagerly(true);
    }

    @Provides
    @NonNull
    @Singleton
    public PersonsService providePersonsService(Retrofit retrofit) {
        return retrofit.create(PersonsService.class);
    }

    @Provides
    @NonNull
    Gson provideGson() {
        return new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

}