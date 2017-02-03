package com.digitalisma.boilerplate.util;

import rx.Subscriber;
import timber.log.Timber;

public abstract class RxSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        // Do nothing
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, "Observable said no!");
    }

    @Override
    public void onNext(T t) {
        // Do nothing
    }
}
