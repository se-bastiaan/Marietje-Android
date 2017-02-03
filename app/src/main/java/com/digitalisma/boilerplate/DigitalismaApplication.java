package com.digitalisma.boilerplate;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.beta.Beta;
import com.digitalisma.boilerplate.injection.component.AppComponent;
import com.digitalisma.boilerplate.injection.component.DaggerAppComponent;
import com.digitalisma.boilerplate.injection.module.AppModule;

import net.ypresto.timbertreeutils.CrashlyticsLogExceptionTree;
import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class DigitalismaApplication extends MultiDexApplication {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Fabric.with(this, new Answers(), new Beta());
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics(), new Answers(), new Beta());
            Timber.plant(new CrashlyticsLogExceptionTree(Log.WARN));
            Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }
    }

    public static DigitalismaApplication get(Context context) {
        return (DigitalismaApplication) context.getApplicationContext();
    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

}
