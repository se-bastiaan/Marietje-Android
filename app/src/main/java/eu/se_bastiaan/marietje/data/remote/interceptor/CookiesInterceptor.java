package eu.se_bastiaan.marietje.data.remote.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.events.NeedsSessionCookie;
import eu.se_bastiaan.marietje.util.EventBus;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Singleton
public class CookiesInterceptor implements Interceptor {

    public static final String SESSION_COOKIE = "sessionid";
    public static final String CSRF_COOKIE = "csrftoken";
    public static final String CSRF_HEADER = "X-CSRFToken";
    public static final String REF_HEADER = "Referer";
    public static final String COOKIE_HEADER = "Cookie";
    private final PreferencesHelper preferencesHelper;
    private final EventBus eventBus;

    @Inject
    public CookiesInterceptor(PreferencesHelper preferencesHelper, EventBus eventBus) {
        this.preferencesHelper = preferencesHelper;
        this.eventBus = eventBus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        String sessionId = preferencesHelper.getSessionId();
        String csrfToken = preferencesHelper.getCsrfToken();
        String cookieHeader = "";
        if (!sessionId.isEmpty()) {
            cookieHeader += String.format(Locale.ENGLISH, "%s=%s;", SESSION_COOKIE, sessionId);
        }
        if (!csrfToken.isEmpty()) {
            cookieHeader +=  String.format(Locale.ENGLISH, "%s=%s;", CSRF_COOKIE, csrfToken);
            builder.header(CSRF_HEADER, csrfToken);
        }
        builder.header(COOKIE_HEADER, cookieHeader);
        builder.header(REF_HEADER, preferencesHelper.getApiUrl());

        Response response = chain.proceed(builder.build());

        List<Cookie> responseCookies = Cookie.parseAll(response.request().url(), response.headers());
        for (Cookie cookie : responseCookies) {
            if (cookie.name().equals(SESSION_COOKIE)) {
                Timber.d("Saving session id: %s", cookie.value());
                sessionId = cookie.value();
                preferencesHelper.setSessionId(cookie.value());
            }
            if (cookie.name().equals(CSRF_COOKIE)) {
                Timber.d("Saving csrf token: %s", cookie.value());
                preferencesHelper.setCsrftoken(cookie.value());
            }
        }

        if (sessionId.isEmpty()) {
            eventBus.post(new NeedsSessionCookie());
        }

        return response;
    }
}