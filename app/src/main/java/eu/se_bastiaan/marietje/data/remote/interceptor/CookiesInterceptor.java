package eu.se_bastiaan.marietje.data.remote.interceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Singleton
public class CookiesInterceptor implements Interceptor {

    private final PreferencesHelper preferencesHelper;

    @Inject
    public CookiesInterceptor(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Set<String> cookies = preferencesHelper.getCookies();
        for (String cookie : cookies) {
            builder.addHeader("Cookie", cookie);
            Timber.d("Adding cookie: %s", cookie);
        }

        Response response = chain.proceed(builder.build());

        if (!response.headers("Set-Cookie").isEmpty()) {
            cookies = new HashSet<>();
            for (String header : response.headers("Set-Cookie")) {
                cookies.add(header);
            }
            preferencesHelper.setCookies(cookies);
        }

        return response;
    }
}