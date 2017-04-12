package eu.se_bastiaan.marietje;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import net.ypresto.timbertreeutils.CrashlyticsLogExceptionTree;
import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import eu.se_bastiaan.marietje.events.NeedsCsrfToken;
import eu.se_bastiaan.marietje.injection.component.AppComponent;
import eu.se_bastiaan.marietje.injection.component.DaggerAppComponent;
import eu.se_bastiaan.marietje.injection.module.AppModule;
import eu.se_bastiaan.marietje.util.Foreground;
import eu.se_bastiaan.marietje.util.RxSubscriber;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MarietjeApp extends MultiDexApplication implements Foreground.Listener {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics(), new Answers());
            Timber.plant(new CrashlyticsLogExceptionTree(Log.WARN));
            Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }

        Foreground.init(this).addListener(this);

        init();
    }

    public void init() {
        getComponent().eventBus().register(NeedsCsrfToken.class)
                .subscribe(new RxSubscriber<NeedsCsrfToken>() {
                    @Override
                    public void onNext(NeedsCsrfToken needsCsrfToken) {
                        getComponent().dataManager().controlDataManager().csrf().subscribe(new RxSubscriber<String>() {
                            @Override
                            public void onError(Throwable e) {
                                Timber.w(e);
                            }

                            @Override
                            public void onNext(String s) {
                                Timber.d("Current CSRF token: " + s);
                            }
                        });
                    }
                });
    }

    public static MarietjeApp get(Context context) {
        return (MarietjeApp) context.getApplicationContext();
    }

    /**
     * For mocking
     */
    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
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

    @Override
    public void onBecameForeground() {
        getComponent().eventBus().post(new NeedsCsrfToken());
    }

    @Override
    public void onBecameBackground() {
        // Do nothing
    }

}
