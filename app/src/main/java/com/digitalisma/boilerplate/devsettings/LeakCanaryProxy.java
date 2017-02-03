package com.digitalisma.boilerplate.devsettings;

import android.support.annotation.NonNull;

public interface LeakCanaryProxy {
    void init();
    void watch(@NonNull Object object);
}