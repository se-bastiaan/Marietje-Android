package eu.se_bastiaan.marietje.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import eu.se_bastiaan.marietje.ui.main.controls.ControlsFragment;
import eu.se_bastiaan.marietje.ui.main.queue.QueueFragment;
import eu.se_bastiaan.marietje.ui.main.request.RequestFragment;
import eu.se_bastiaan.marietje.ui.other.ViewModifier;
import eu.se_bastiaan.marietje.util.PixelUtil;
import eu.se_bastiaan.marietje.util.TextUtil;

public class MainActivity extends BaseActivity implements MainView {

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
        return new Intent(context, MainActivity.class);
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
            // TODO: Remove when other functionality is added
            if (item.getItemId() != R.id.tab_request) {
                Snackbar.make(containerLayout, "Sorry, this functionality is not available yet", Snackbar.LENGTH_SHORT).show();
                bottomNavigationView.post(() -> ((BottomNavigationMenuView) bottomNavigationView.getChildAt(0)).getChildAt(1).callOnClick());
                return true;
            }

            changeTabFragment(item.getItemId());
            return true;
        });

//        changeTabFragment(R.id.tab_queue); TODO: Uncomment when functionality is added
        ((BottomNavigationMenuView) bottomNavigationView.getChildAt(0)).getChildAt(1).callOnClick(); // TODO: Remove when other functionality is added
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

    private void showMissingFunctionality() {
        Snackbar.make(containerLayout, "Sorry, this functionality is not available yet", Snackbar.LENGTH_SHORT).show();
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
            case R.id.tab_controls:
                setTitle(R.string.controls);
                if (!(currentFragment instanceof ControlsFragment))
                    fragment = ControlsFragment.newInstance();
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(this);
    }

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

}
