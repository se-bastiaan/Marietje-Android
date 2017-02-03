package com.digitalisma.boilerplate.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.R;
import com.digitalisma.boilerplate.injection.component.FragmentComponent;
import com.digitalisma.boilerplate.injection.module.FragmentModule;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    @NonNull
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    @BindView(R.id.rootview)
    protected View rootView;

    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, @LayoutRes Integer layoutRes) {
        BaseActivity activity = (BaseActivity) getActivity();
        injectFragment(activity.getActivityComponent().fragmentComponent(new FragmentModule(activity)));

        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroy() {
        DigitalismaApplication.get(getActivity()).getComponent().leakCanaryProxy().watch(this);
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
