package eu.se_bastiaan.marietje.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.injection.component.ActivityComponent;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.ui.base.BaseActivity;
import eu.se_bastiaan.marietje.ui.login.LoginActivity;
import eu.se_bastiaan.marietje.ui.main.queue.QueueFragment;
import eu.se_bastiaan.marietje.ui.main.request.RequestFragment;
import eu.se_bastiaan.marietje.ui.other.ViewModifier;
import eu.se_bastiaan.marietje.util.PixelUtil;
import eu.se_bastiaan.marietje.util.TextUtil;

public class MainActivity extends BaseActivity implements MainView {

    public static final String EXTRA_AUTO_REFRESH_QUEUE = "auto_refresh";
    private Fragment currentFragment;

    @Inject
    MainPresenter presenter;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    @Named(DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER)
    ViewModifier viewModifier;

    @BindView(R.id.container)
    FrameLayout containerLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_drawer_layout)
    View rootView;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return getStartIntent(context, true);
    }

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context, boolean autoRefresh) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_AUTO_REFRESH_QUEUE, autoRefresh);
        return intent;
    }

    @SuppressLint("InflateParams") // In this case it's ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));
        ButterKnife.bind(this);
        presenter.attachView(this);

        if (TextUtil.isEmpty(preferencesHelper.getSessionId())) {
            showLogin();
            return;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            changeTabFragment(item.getItemId());
            return true;
        });

        changeTabFragment(R.id.tab_queue);
        checkKeyBoardUp();
}

    private void checkKeyBoardUp() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
            if (heightDiff > PixelUtil.getPixelsFromDp(this, 200)) { // if more than 200 dp, its probably a keyboard...
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeTabFragment(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.tab_queue:
                setTitle(R.string.queue);
                if (!(currentFragment instanceof QueueFragment))
                    fragment = QueueFragment.newInstance();
                break;
            case R.id.tab_request:
                setTitle(R.string.request);
                if (!(currentFragment instanceof RequestFragment))
                    fragment = RequestFragment.newInstance();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            currentFragment = fragment;
        }
    }

    @Override
    public void showLogin() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_controls:
                // TODO: Add functionality to control audio
                Snackbar.make(containerLayout.findViewById(R.id.recycler_view), R.string.functionality_missing, Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                preferencesHelper.clear();
                showLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(this);
    }

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

}
