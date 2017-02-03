package com.digitalisma.boilerplate.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.digitalisma.boilerplate.DigitalismaApplication;
import com.digitalisma.boilerplate.injection.component.ActivityComponent;
import com.digitalisma.boilerplate.injection.component.ConfigPersistentComponent;
import com.digitalisma.boilerplate.injection.component.DaggerConfigPersistentComponent;
import com.digitalisma.boilerplate.injection.module.ActivityModule;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent> COMPONENT_MAP = new LongSparseArray<>();

    private long activityId;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createInjectionComponent(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState, @LayoutRes int layoutRes) {
        super.onCreate(savedInstanceState);
        createInjectionComponent(savedInstanceState);
        setContentView(layoutRes);
        ButterKnife.bind(this);
    }

    public void proceedOnCreate(View v) {
        setContentView(v);
        ButterKnife.bind(this);
    }

    private void createInjectionComponent(Bundle savedInstanceState) {
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (COMPONENT_MAP.indexOfKey(activityId) < 0) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(DigitalismaApplication.get(this).getComponent())
                    .build();
            COMPONENT_MAP.put(activityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = COMPONENT_MAP.get(activityId);
        }
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
        injectActivity(activityComponent);
    }

    @Override
    protected void onDestroy() {
        DigitalismaApplication.get(this).getComponent().leakCanaryProxy().watch(this);
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    protected void injectActivity(ActivityComponent component) {
        // Override in case you want to inject
    }

}
