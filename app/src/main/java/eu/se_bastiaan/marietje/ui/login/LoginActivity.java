package eu.se_bastiaan.marietje.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import butterknife.BindView;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.injection.component.ActivityComponent;
import eu.se_bastiaan.marietje.ui.base.BaseActivity;
import eu.se_bastiaan.marietje.ui.main.MainActivity;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    LoginPresenter presenter;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.web_view)
    WebView webView;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
        presenter.attachView(this);

        refreshLayout.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.contains("login") && !url.contains("logout")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    presenter.saveCookies(cookies);
                    Timber.d("Cookies: %s", cookies);
                }
                refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setOnRefreshListener(() -> presenter.loadData());

        presenter.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(this);
    }

    @Override
    public void showLogin(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void showLoginSuccessful() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

}
