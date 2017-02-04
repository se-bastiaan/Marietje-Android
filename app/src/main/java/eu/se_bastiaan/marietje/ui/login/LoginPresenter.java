package eu.se_bastiaan.marietje.ui.login;

import android.content.Context;

import java.util.HashSet;
import java.util.Locale;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.remote.interceptor.CookiesInterceptor;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.PerActivity;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;

@PerActivity
public class LoginPresenter extends BasePresenter<LoginView> {

    private final DataManager dataManager;
    private final Context context;

    @Inject
    public LoginPresenter(DataManager dataManager, @ApplicationContext  Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

    public void loadData() {
        view().showLogin(String.format(Locale.ENGLISH, "%s%s", dataManager.preferencesHelper().getApiUrl(), "logout"));
    }

    public void saveCookies(String cookieStr) {
        HashSet<String> cookieSet = new HashSet<>();
        String[] cookies = cookieStr.split(";");
        boolean hasSessionCookie = false;
        for (String cookie : cookies) {
            if (!hasSessionCookie) {
                hasSessionCookie = cookie.contains(CookiesInterceptor.SESSION_COOKIE);
            }
            cookieSet.add(cookie.trim());
        }

        if (hasSessionCookie) {
            this.dataManager.preferencesHelper().setCookies(cookieSet);
            view().showLoginSuccessful();
        }
    }

}
