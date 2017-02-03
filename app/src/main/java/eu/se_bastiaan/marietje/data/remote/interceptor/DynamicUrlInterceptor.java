package eu.se_bastiaan.marietje.data.remote.interceptor;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class DynamicUrlInterceptor implements Interceptor {

    private final PreferencesHelper preferencesHelper;

    @Inject
    public DynamicUrlInterceptor(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!preferencesHelper.getApiUrl().isEmpty()) {
            HttpUrl.Builder url = HttpUrl.parse(preferencesHelper.getApiUrl()).newBuilder();

            for (String pathSegment : request.url().pathSegments()) {
                url.addPathSegment(pathSegment);
            }

            for (int i = 0; i < request.url().querySize(); i++) {
                String name = request.url().queryParameterName(i);
                String value = request.url().queryParameterValue(i);
                url.addQueryParameter(name, value);
            }

            request = request.newBuilder()
                    .url(url.build())
                    .build();
        }

        return chain.proceed(request);
    }

}
