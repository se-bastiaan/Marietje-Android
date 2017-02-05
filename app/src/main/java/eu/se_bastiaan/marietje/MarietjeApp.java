package eu.se_bastiaan.marietje;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.beta.Beta;

import net.ypresto.timbertreeutils.CrashlyticsLogExceptionTree;
import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import eu.se_bastiaan.marietje.injection.component.AppComponent;
import eu.se_bastiaan.marietje.injection.component.DaggerAppComponent;
import eu.se_bastiaan.marietje.injection.module.AppModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MarietjeApp extends MultiDexApplication {

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

    public static MarietjeApp get(Context context) {
        return (MarietjeApp) context.getApplicationContext();
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
