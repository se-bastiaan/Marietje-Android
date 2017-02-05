package eu.se_bastiaan.marietje.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.injection.module.FragmentModule;


public abstract class BaseFragment extends Fragment {

    @NonNull
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, @LayoutRes Integer layoutRes) {
        BaseActivity activity = (BaseActivity) getActivity();
        injectFragment(activity.getActivityComponent().fragmentComponent(new FragmentModule()));

        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getActivity() instanceof BaseActivity && toolbar != null) {
            ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onDestroy() {
        MarietjeApp.get(getActivity()).getComponent().leakCanaryProxy().watch(this);
        super.onDestroy();
    }

    protected void runOnUiThreadIfFragmentAlive(@NonNull final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper() && isFragmentAlive()) {
            runnable.run();
        } else {
            MAIN_THREAD_HANDLER.post(() -> {
                if (isFragmentAlive()) {
                    runnable.run();
                }
            });
        }
    }

    private boolean isFragmentAlive() {
        return getActivity() != null && isAdded() && !isDetached() && getView() != null && !isRemoving();
    }

    protected void injectFragment(FragmentComponent component) {
        // Override in case you want to inject
    }

}
