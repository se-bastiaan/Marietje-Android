package eu.se_bastiaan.marietje.devsettings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import eu.se_bastiaan.marietje.MarietjeApp;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryProxyImpl implements LeakCanaryProxy {

    @NonNull
    private final MarietjeApp app;

    @Nullable
    private RefWatcher refWatcher;

    public LeakCanaryProxyImpl(@NonNull MarietjeApp app) {
        this.app = app;
    }

    @Override
    public void init() {
        refWatcher = LeakCanary.install(app);
    }

    @Override
    public void watch(@NonNull Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }
}