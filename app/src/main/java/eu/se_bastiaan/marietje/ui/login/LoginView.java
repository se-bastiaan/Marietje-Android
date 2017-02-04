package eu.se_bastiaan.marietje.ui.login;

import eu.se_bastiaan.marietje.ui.base.MvpView;

public interface LoginView extends MvpView {

    void showLogin(String url);
    void showLoginSuccessful();

}
