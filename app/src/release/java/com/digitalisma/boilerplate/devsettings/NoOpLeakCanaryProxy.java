package eu.se_bastiaan.marietje.devsettings;

import android.support.annotation.NonNull;

public class NoOpLeakCanaryProxy implements LeakCanaryProxy {
    @Override
    public void init() {
        // no-op.
    }

    @Override
    public void watch(@NonNull Object object) {
        // no-op.
    }
}