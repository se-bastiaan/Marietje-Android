package eu.se_bastiaan.marietje.ui.login;

import android.content.Context;

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
        if (cookieStr == null) {
            return;
        }

        String[] cookies = cookieStr.split(";");
        for (String cookie : cookies) {
            String[] cookieValue = cookie.split("=");
            if (cookieValue[0].equals(CookiesInterceptor.SESSION_COOKIE)) {
                this.dataManager.preferencesHelper().setSessionId(cookieValue[1]);
            } else if (cookieValue[0].equals(CookiesInterceptor.CSRF_COOKIE)) {
                this.dataManager.preferencesHelper().setCsrftoken(cookieValue[1]);
            }
        }

        if (!this.dataManager.preferencesHelper().getSessionId().isEmpty()) {
            view().showLoginSuccessful();
        }
    }

}
